package com.example.demoSpringBoot.service;

import com.example.demoSpringBoot.entity.Calories;
import com.example.demoSpringBoot.entity.ChartScore;
import com.example.demoSpringBoot.entity.Sale;
import com.example.demoSpringBoot.entity.Score;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.crosstabs.design.*;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyReportService {
    MessageSource msgSource;
    ScoreService scoreService;
    SaleService saleService;

    public ByteArrayOutputStream generateReport(Locale locale) throws JRException {
        String filePath = "W:\\WORKSPACE\\javaworkspace\\demoSpringBoot\\src\\main\\resources\\MyTestReport.jrxml";

        List<Score> scores = scoreService.getScores();
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(scores);

        List<ChartScore> chartScores = new ArrayList<>();
        chartScores.add(new ChartScore("Mix", 10));
        chartScores.add(new ChartScore("Sue", 2));
        chartScores.add(new ChartScore("Min", 4));
        chartScores.add(new ChartScore("Ask", 1));
        chartScores.add(new ChartScore("Misu", 1));
        chartScores.add(new ChartScore("Rose", 7));
        JRBeanCollectionDataSource cds = new JRBeanCollectionDataSource(chartScores);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("MyReport_Reporter", "Joss");
        params.put("MyReport_DS", ds);
        params.put("MyReport_PieChartDS", cds);

        // Sub report
        params.put("CaloriesReport", getCaloriesReport());
        params.put("CaloriesParameters", getCaloriesParameters());

        // Set localized parameters
        setLocalizedParameters(params, locale);

        // Compile
        JasperReport jasperReport = JasperCompileManager.compileReport(filePath);
        // Alter report element programmatically using key defined in JRXML file.
        JRStaticText dateStr = (JRStaticText) jasperReport.getPageHeader().getElementByKey("pageHeaderDateStr");
        dateStr.setForecolor(Color.RED);
        dateStr.setText("Report date: ");
        // Fill
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

        /*
        // Export
        JasperExportManager.exportReportToPdfFile(jasperPrint, "W:\WORKSPACE\javaworkspace\demoJasper\src\main\resources\\MyTestReport.pdf");
        System.out.println("Done");
        */

        // Export to pdf
        return exportReportToPDF(jasperPrint);
    }

    public ByteArrayOutputStream designReportByLib() throws JRException {
        List<Sale> s = saleService.getSales();
        JRBeanCollectionDataSource cds = new JRBeanCollectionDataSource(s);

        //Design
        JasperDesign jasperDesign = designReport();
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        //Export JRXML file
        String outputPath = "W:\\WORKSPACE\\javaworkspace\\demoSpringBoot\\src\\main\\resources\\output.jrxml";
        JRXmlWriter.writeReport(jasperReport, outputPath, "UTF-8");
        // Export
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, cds);
        return exportReportToPDF(jasperPrint);
    }

    private JRBeanCollectionDataSource getCaloriesDatasource() {
        List<Calories> elems = new ArrayList<>();
        elems.add(new Calories("breakfast", 300));
        elems.add(new Calories("lunch", 500));
        elems.add(new Calories("dinner", 300));
        return new JRBeanCollectionDataSource(elems);
    }

    private Map<String, Object> getCaloriesParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("Calories_DS", getCaloriesDatasource());
        return params;
    }

    private JasperReport getCaloriesReport() {
        String filePath = "W:\\WORKSPACE\\javaworkspace\\demoSpringBoot\\src\\main\\resources\\CaloriesReport.jrxml";
        JasperReport report;
        try {
            report = JasperCompileManager.compileReport(filePath);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    private void setLocalizedParameters(Map<String, Object> params, Locale locale) {
        MessageSourceResourceBundle messageSourceResourceBundle = new MessageSourceResourceBundle(msgSource, locale);
        params.put(JRParameter.REPORT_RESOURCE_BUNDLE, messageSourceResourceBundle);
        params.put(JRParameter.REPORT_LOCALE, locale);
    }

    private JasperDesign designReport() throws JRException {
        // Template
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("My designed report");
        jasperDesign.setPageHeight(842);
        jasperDesign.setPageWidth(595);

        jasperDesign.addStyle(addStyle("Crosstab_CT", ModeEnum.OPAQUE, Color.LIGHT_GRAY));
        jasperDesign.addStyle(addStyle("Crosstab_CH", ModeEnum.OPAQUE, Color.BLUE));

        JRDesignBand summaryBand = new JRDesignBand();
        summaryBand.setHeight(700);

        // Field
        JRDesignField regionF = new JRDesignField();
        regionF.setName("region");
        regionF.setValueClass(String.class);
        jasperDesign.addField(regionF);
        JRDesignField productF = new JRDesignField();
        productF.setName("product");
        productF.setValueClass(String.class);
        jasperDesign.addField(productF);
        JRDesignField saleF = new JRDesignField();
        saleF.setName("sale");
        saleF.setValueClass(Integer.class);
        jasperDesign.addField(saleF);

        //Cross tab
        JRDesignCrosstab crosstab = new JRDesignCrosstab();
        crosstab.setX(40);
        crosstab.setY(10);
        crosstab.setWidth(500);
        crosstab.setHeight(300);
        crosstab.setBackcolor(Color.LIGHT_GRAY);


        //RowGroup
        crosstab.addRowGroup(genCrosstabRowGroup());

        //Column Group
        crosstab.addColumnGroup(genCrosstabColumnGroup());

        //Measurement
        crosstab.addMeasure(genCrosstabMeasure());

        //Cell
        JRDesignCrosstabCell cell1 = genCrosstabCell();
        crosstab.addCell(cell1);
        JRDesignCrosstabCell cell2 = genCrosstabCell();
        cell2.setColumnTotalGroup("region");
        crosstab.addCell(cell2);
        JRDesignCrosstabCell cell3 = genCrosstabCell();
        cell3.setRowTotalGroup("product");
        crosstab.addCell(cell3);
        JRDesignCrosstabCell cell4 = genCrosstabCell();
        cell4.setRowTotalGroup("product");
        cell4.setColumnTotalGroup("region");
        crosstab.addCell(cell4);

        summaryBand.addElement(crosstab);
        jasperDesign.setSummary(summaryBand);
        return jasperDesign;
    }

    private ByteArrayOutputStream exportReportToPDF(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimplePdfExporterConfiguration pdfExporterConfiguration = new SimplePdfExporterConfiguration();
        pdfExporterConfiguration.setCompressed(true);
        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setConfiguration(pdfExporterConfiguration);
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        pdfExporter.exportReport();
        return byteArrayOutputStream;
    }

    private JRDesignCrosstabRowGroup genCrosstabRowGroup() {
        JRDesignCrosstabRowGroup rowGroup = new JRDesignCrosstabRowGroup();
        rowGroup.setName("product");
        JRDesignCrosstabBucket bk = new JRDesignCrosstabBucket();
        bk.setExpression(new JRDesignExpression("$F{product}"));
        bk.setValueClassName("java.lang.String");
        rowGroup.setBucket(bk);

        JRDesignTextField rowHeaderTextField = new JRDesignTextField();
        rowHeaderTextField.setWidth(60);
        rowHeaderTextField.setHeight(20);
        rowHeaderTextField.setExpression(new JRDesignExpression("$V{product}"));
//        JRLineBox b = rowHeaderTextField.getLineBox();
//        b.getLeftPen().setLineWidth(.5f);
//        b.getLeftPen().setLineColor(Color.BLACK);
//        b.getRightPen().setLineWidth(.5f);
//        b.getRightPen().setLineColor(Color.BLACK);
//        b.getTopPen().setLineWidth(.5f);
//        b.getTopPen().setLineColor(Color.BLACK);
//        b.getBottomPen().setLineWidth(.5f);
//        b.getBottomPen().setLineColor(Color.BLACK);
        setLineFormat(rowHeaderTextField.getLineBox());
        JRDesignCellContents rowHeaderCellContent = new JRDesignCellContents();
        rowHeaderCellContent.addElement(rowHeaderTextField);
        rowHeaderCellContent.setMode(ModeEnum.OPAQUE);
        rowHeaderCellContent.setStyle(addStyle("Crosstab_CT", null, null));
        rowGroup.setHeader(rowHeaderCellContent);

        JRDesignStaticText rowTotalHeaderTextField = new JRDesignStaticText();
        rowTotalHeaderTextField.setWidth(60);
        rowTotalHeaderTextField.setHeight(20);
        rowTotalHeaderTextField.setText("Total product");
        setLineFormat(rowTotalHeaderTextField.getLineBox());
        JRDesignCellContents rowTotalHeaderCellContent = new JRDesignCellContents();
        rowTotalHeaderCellContent.addElement(rowTotalHeaderTextField);
        rowTotalHeaderCellContent.setMode(ModeEnum.OPAQUE);
        rowTotalHeaderCellContent.setStyle(addStyle("Crosstab_CT", null, null));
        rowGroup.setTotalHeader(rowTotalHeaderCellContent);

        rowGroup.setWidth(60);
        rowGroup.setTotalPosition(CrosstabTotalPositionEnum.END);
        return rowGroup;
    }

    private JRDesignCrosstabColumnGroup genCrosstabColumnGroup() {
        JRDesignCrosstabColumnGroup columnGroup = new JRDesignCrosstabColumnGroup();
        columnGroup.setName("region");
        JRDesignCrosstabBucket bk1 = new JRDesignCrosstabBucket();
        bk1.setExpression(new JRDesignExpression("$F{region}"));
        bk1.setValueClassName("java.lang.String");
        columnGroup.setBucket(bk1);

        JRDesignTextField rowHeaderTextField = new JRDesignTextField();
        rowHeaderTextField.setWidth(60);
        rowHeaderTextField.setHeight(20);
        rowHeaderTextField.setExpression(new JRDesignExpression("$V{region}"));
        setLineFormat(rowHeaderTextField.getLineBox());
        JRDesignCellContents rowHeaderCellContent = new JRDesignCellContents();
        rowHeaderCellContent.addElement(rowHeaderTextField);
        rowHeaderCellContent.setMode(ModeEnum.OPAQUE);
        JRDesignStyle style = new JRDesignStyle();
        rowHeaderCellContent.setStyle(addStyle("Crosstab_CH", null, null));
        columnGroup.setHeader(rowHeaderCellContent);

        JRDesignStaticText rowTotalHeaderTextField = new JRDesignStaticText();
        rowTotalHeaderTextField.setWidth(60);
        rowTotalHeaderTextField.setHeight(20);
        rowTotalHeaderTextField.setText("Total region");
        setLineFormat(rowTotalHeaderTextField.getLineBox());
        JRDesignCellContents rowTotalHeaderCellContent = new JRDesignCellContents();
        rowTotalHeaderCellContent.addElement(rowTotalHeaderTextField);
        rowTotalHeaderCellContent.setMode(ModeEnum.OPAQUE);
        rowTotalHeaderCellContent.setStyle(addStyle("Crosstab_CH", null, null));
        columnGroup.setTotalHeader(rowTotalHeaderCellContent);
        columnGroup.setHeight(20);
        columnGroup.setTotalPosition(CrosstabTotalPositionEnum.END);
        return columnGroup;
    }

    private JRDesignCrosstabMeasure genCrosstabMeasure() {
        JRDesignCrosstabMeasure measure = new JRDesignCrosstabMeasure();
        measure.setName("sale_MEASURE");
        measure.setValueClassName("java.lang.Integer");
        measure.setValueExpression(new JRDesignExpression("$F{sale}"));
        measure.setCalculation(CalculationEnum.SUM);
        return measure;
    }

    private JRDesignCrosstabCell genCrosstabCell() {
        JRDesignCrosstabCell crosstabCell = new JRDesignCrosstabCell();
        crosstabCell.setWidth(60);
        crosstabCell.setHeight(20);

        JRDesignTextField textField1 = new JRDesignTextField();
        textField1.setWidth(60);
        textField1.setHeight(20);
        textField1.setExpression(new JRDesignExpression("$V{sale_MEASURE}"));
        JRDesignCellContents cellContent1 = new JRDesignCellContents();
        cellContent1.addElement(textField1);
        cellContent1.setMode(ModeEnum.OPAQUE);
        crosstabCell.setContents(cellContent1);
        return crosstabCell;
    }

    private JRDesignStyle addStyle(String name, ModeEnum mode, Color color) {
        JRDesignStyle s = new JRDesignStyle();
        s.setName(name);
        s.setMode(mode);
        s.setBackcolor(color);
        return s;
    }

    private void setLineFormat(JRLineBox b) {
        b.getLeftPen().setLineWidth(.5f);
        b.getLeftPen().setLineColor(Color.BLACK);
        b.getRightPen().setLineWidth(.5f);
        b.getRightPen().setLineColor(Color.BLACK);
        b.getTopPen().setLineWidth(.5f);
        b.getTopPen().setLineColor(Color.BLACK);
        b.getBottomPen().setLineWidth(.5f);
        b.getBottomPen().setLineColor(Color.BLACK);
    }
}
