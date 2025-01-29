package com.pas.pdfstuff;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.*;

public class OpenPDFExample
{

    public static void main (String[] args)
    {

        // creation of the document with a certain size and certain margins
        // (you can use PageSize.Letter instead of PageSize.A4)
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try
        {
            // creation of the different writers
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("c:/Temp/OpenPDFExample.pdf"));

            // various fonts
            BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
            BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
            BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252", false);
            BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);

            // headers and footers must be added before the document is opened
            HeaderFooter footer = new HeaderFooter(
                        new Phrase("This is page: ", new Font(bf_courier)), true);
            footer.setBorder(Rectangle.NO_BORDER);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.setFooter(footer);

            HeaderFooter header = new HeaderFooter(
                        new Phrase("This is a header without a page number", new Font(bf_symbol)), false);
            header.setAlignment(Element.ALIGN_CENTER);
            document.setHeader(header);

            document.open();

            int y_line1 = 650;
            int y_line2 = y_line1 - 50;
            int y_line3 = y_line2 - 50;

            // draw a few lines ...
            PdfContentByte cb = writer.getDirectContent();
            cb.setLineWidth(0f);
            cb.moveTo(250, y_line3 - 100);
            cb.lineTo(250, y_line1 + 100);
            cb.moveTo(50, y_line1);
            cb.lineTo(400, y_line1);
            cb.moveTo(50, y_line2);
            cb.lineTo(400, y_line2);
            cb.moveTo(50, y_line3);
            cb.lineTo(400, y_line3);
            cb.stroke();
            // ... and some text that is aligned in various ways
            cb.beginText();
            cb.setFontAndSize(bf_helv, 12);
            String text = "Sample text for alignment";
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text + " Center", 250, y_line1, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, text + " Right", 250, y_line2, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text + " Left", 250, y_line3, 0);
            cb.endText();

            // add an image, scale it down by half, and put at an absolute position
            try {
                Image simple = Image.getInstance("c:/paul/github/slomfin/src/main/webapp/images/PaulSmall.bmp");
                simple.setAbsolutePosition(100, 100);
                simple.scalePercent(50);
                document.add(simple);
            } catch (Exception ex) {
                System.out.println("no image for you");
            }

            // start second page
            document.newPage();

            // add text in three paragraphs from top to bottom with various font styles
            Paragraph par = new Paragraph("bold paragraph");
            par.getFont().setStyle(Font.BOLD);
            document.add(par);
            par = new Paragraph("italic paragraph");
            par.getFont().setStyle(Font.ITALIC);
            document.add(par);
            par = new Paragraph("underlined and strike-through paragraph");
            par.getFont().setStyle(Font.UNDERLINE | Font.STRIKETHRU);
            document.add(par);

            // demonstrate some table features
            Table table = new Table(3);
                // 2 pixel wide blue border
            table.setBorderWidth(2);
            table.setBorderColor(new Color(0, 0, 255));
            table.setPadding(5);
            table.setSpacing(5);
            Cell c = new Cell("header");
            c.setHeader(true);
            c.setColspan(3);
            table.addCell(c);
            table.endHeaders();
            c = new Cell("example cell with rowspan 2 and red border");
            c.setRowspan(2);
            c.setBorderColor(new Color(255, 0, 0));
            table.addCell(c);
            table.addCell("1.1");
            table.addCell("2.1");
            table.addCell("1.2");
            table.addCell("2.2");
            c = new Cell("align center");
            c.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.addCell(c);
            Cell cell = new Cell("big cell");
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            c = new Cell("align right");
            c.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            table.addCell(c);
            document.add(table);

            // add text at an absolute position
            cb.beginText();
            cb.setFontAndSize(bf_times, 14);
            cb.setTextMatrix(100, 300);
            cb.showText("Text at position 100, 300.");
            cb.endText();

            // rotated text at an absolute position
            PdfTemplate template = cb.createTemplate(300, 300);                
            template.beginText();
            template.setFontAndSize(bf_times, 14);
            template.showText("Rotated text at position 400, 200.");
            template.endText();

            float rotate = 90;
            float x = 400;
            float y = 200;
            float angle  = (float) (-rotate * (Math.PI / 180));
            float xScale = (float) Math.cos(angle);
            float yScale = (float) Math.cos(angle);
            float xRot   = (float) -Math.sin(angle);
            float yRot   = (float) Math.sin(angle);

            cb.addTemplate(template, xScale, xRot, yRot, yScale, x, y);

            // we're done!
            document.close();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}