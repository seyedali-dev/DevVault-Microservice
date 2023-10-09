package com.dev.vault.TaskService.service.module.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class EntityHeaderAndFooterEventHelper extends PdfPageEventHelper {

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfPCell emptyCell = new PdfPCell(new Paragraph("--------"));
        emptyCell.setBorder(Rectangle.BOX);

        Font titleFont = new Font(Font.COURIER, 20, Font.BOLD);
        Paragraph title = new Paragraph("Seyed Ali.", titleFont);
        PdfPCell titleCell = new PdfPCell(title);
        titleCell.setPaddingBottom(10);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.NO_BORDER);
    }


    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        /* Footer */
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(510);
        table.setWidths(new int[]{50, 50});
        // Magic about default cell - if you add styling to default cell it will apply to all cells except cells added using addCell(PdfPCell) method.
        table.getDefaultCell().setPaddingBottom(5);
        table.getDefaultCell().setBorder(Rectangle.TOP);

        Paragraph title = new Paragraph("Seyed Ali.", new Font(Font.HELVETICA, 10));
        PdfPCell titleCell = new PdfPCell(title);
        titleCell.setPaddingTop(4);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleCell.setBorder(Rectangle.TOP);
        table.addCell(titleCell);

        Paragraph pageNumberText = new Paragraph("Page " + document.getPageNumber(), new Font(Font.HELVETICA, 10));
        PdfPCell pageNumberCell = new PdfPCell(pageNumberText);
        pageNumberCell.setPaddingTop(4);
        pageNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pageNumberCell.setBorder(Rectangle.TOP);
        table.addCell(pageNumberCell);

        // write the table on PDF
        table.writeSelectedRows(0, -1, 34, 36, writer.getDirectContent());

    }

}
