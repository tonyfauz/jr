package com.ls.jr.core;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.ls.jr.JrTestContainer;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.security.entity.User;
import com.ls.jr.entity.Report;
import com.ls.jr.entity.ReportFile;
import com.ls.jr.entity.TipoExport;
import com.ls.jr.exceptions.report.PrintFailedException;
import com.ls.jr.service.ReportService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.*;
import java.util.*;

public class ReportIntegrationTest {

    @RegisterExtension
    public static JrTestContainer cont = JrTestContainer.Common.INSTANCE;

    private static Metadata metadata;
    private static Persistence persistence;
    private static DataManager dataManager;
    private static FileStorageAPI fileStorageAPI;

    private static FileDescriptor fdPdf;
    private static FileDescriptor fdPdfsub;
    private static FileDescriptor fdExcel;

    private static ReportService reportService;

    @BeforeAll
    public static void beforeAll() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);
        fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        reportService = AppBeans.get(ReportService.class);


        createFileDescriptorPdf();
        createFileDescriptorExcel();



    }

    private static void createFileDescriptorPdf(){
        fdPdf = metadata.create(FileDescriptor.class);
        fdPdf.setId(UUID.randomUUID());
        fdPdf.setName("test pdf");
        fdPdf.setExtension("jrxml");
        fdPdf.setCreateDate(new Date());
        File file  = new File("C:\\Users\\Tony\\Documents\\workspaceCuba7\\jr\\modules\\core\\build\\test-home\\test-pdf.jrxml");

        fdPdfsub = metadata.create(FileDescriptor.class);
        fdPdfsub.setId(UUID.randomUUID());
        fdPdfsub.setName("sub pdf");
        fdPdfsub.setExtension("jrxml");
        fdPdfsub.setCreateDate(new Date());
        File file2  = new File("C:\\Users\\Tony\\Documents\\workspaceCuba7\\jr\\modules\\core\\build\\test-home\\Blank_A4_Landscape.jrxml");

        try {
            fileStorageAPI.saveStream(fdPdfsub,new FileInputStream(file2));
            fileStorageAPI.saveStream(fdPdf,new FileInputStream(file));
        } catch (FileStorageException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void createFileDescriptorExcel(){
        fdExcel = metadata.create(FileDescriptor.class);
        fdExcel.setId(UUID.randomUUID());
        fdExcel.setName("test excel");
        fdExcel.setExtension("jrxml");
        fdExcel.setCreateDate(new Date());
        File file  = new File("C:\\Users\\Tony\\Documents\\workspaceCuba7\\jr\\modules\\core\\build\\test-home\\test-excel.jrxml");
        try {
            fileStorageAPI.saveStream(fdExcel,new FileInputStream(file));
        } catch (FileStorageException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void afterAll() throws Exception {

    }

    @Test
    public void testPrintPdf() {
        Report report = metadata.create(Report.class);
        List<ReportFile> files = new ArrayList<>();

        ReportFile reportFile = metadata.create(ReportFile.class);
        reportFile.setFile(fdPdf);
        reportFile.setReport(report);
        reportFile.setSubReport(false);

        files.add(reportFile);

        ReportFile reportFileSub = metadata.create(ReportFile.class);
        reportFileSub.setFile(fdPdfsub);
        reportFileSub.setReport(report);
        reportFileSub.setSubReport(true);

        files.add(reportFileSub);

        report.setFiles(files);
        report.setNome("Test Pdf");
        report.setTipo(TipoExport.PDF);
         try {
        byte[] file =  reportService.printReport(report,new HashMap<>());
        File fcreated = new File("C:\\Users\\Tony\\Documents\\workspaceCuba7\\jr\\modules\\core\\build\\test-home\\testpdf.pdf");

        FileOutputStream fileOutputStream = new FileOutputStream(fcreated);
        fileOutputStream.write(file);
        fileOutputStream.flush();
        fileOutputStream.close();
        Assertions.assertTrue(fcreated.exists());

        } catch (PrintFailedException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (FileStorageException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (FileNotFoundException e) {
             e.printStackTrace();
             Assertions.fail(e.getMessage());
         } catch (IOException e) {
             e.printStackTrace();
             Assertions.fail(e.getMessage());
         }


    }
    @Test
    public void testPrintExcel() {

        Report report = metadata.create(Report.class);

        List<ReportFile> files = new ArrayList<>();
        ReportFile rf = metadata.create(ReportFile.class);
        rf.setFile(fdExcel);
        rf.setSubReport(false);
        rf.setReport(report);
        files.add(rf);
        report.setFiles(files);

        report.setNome("Test Excel");
        report.setTipo(TipoExport.EXCEL);

        try {
            byte[] file =  reportService.printReport(report,new HashMap<>());
            File fcreated = new File("C:\\Users\\Tony\\Documents\\workspaceCuba7\\jr\\modules\\core\\build\\test-home\\testexcel.xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(fcreated);
            fileOutputStream.write(file);
            fileOutputStream.flush();
            fileOutputStream.close();

            Assertions.assertTrue(fcreated.exists());

        } catch (PrintFailedException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (FileStorageException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }

    }
   // @Test
    public void testByAlias() {
        Report report = metadata.create(Report.class);
        List<ReportFile> files = new ArrayList<>();
        ReportFile rf = metadata.create(ReportFile.class);
        rf.setFile(fdExcel);
        rf.setSubReport(false);
        rf.setReport(report);

        report.setFiles(files);
        report.setNome("Test Excel");
        report.setAlias("aliasExcel");
        report.setTipo(TipoExport.EXCEL);

        CommitContext commitContext = new CommitContext();
        commitContext.addInstanceToCommit(fdExcel);
        commitContext.addInstanceToCommit(report);
        dataManager.commit(commitContext);

        try {
            byte[] file =  reportService.printCurrentReportFromAlias("aliasExcel",new HashMap<>());
            File fcreated = new File("C:\\Users\\Tony\\Documents\\workspaceCuba7\\jr\\modules\\core\\build\\test-home\\testexcelAlias.xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(fcreated);
            fileOutputStream.write(file);
            fileOutputStream.flush();
            fileOutputStream.close();

            Assertions.assertTrue(fcreated.exists());

        } catch (PrintFailedException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (FileStorageException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }finally {
            dataManager.remove(report);
        }
    }


}