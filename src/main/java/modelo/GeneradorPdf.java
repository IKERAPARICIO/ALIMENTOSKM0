package modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Clase para generar documentos PDF
 * @author Iker Aparicio
 */
public class GeneradorPdf {
	private static String ENTIDAD = "Ayuntamiento de Trapagaran";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLDITALIC);

    /**
     * Completa el fichero PDF pasado con los datos del paquete indicado
     * @param document: fichero PDF
     * @param idPaquete: id del paquete
     * @throws DocumentException
     */
    public void crearReciboPaquete(Document document, int idPaquete) throws DocumentException {
    	this.addMetaData(document);
    	this.addTitle(document,"Propuesta Aceptada por el "+ENTIDAD);
    	this.addContentPropuesta(document,idPaquete);
    	this.addFooter(document);
    }
    
    /**
     * Completa el fichero PDF pasado con los datos de la cesta indicada
     * @param document: fichero PDF
     * @param idCesta: id de la cesta
     * @throws DocumentException
     */
    public void crearReciboCesta(Document document, int idCesta) throws DocumentException {
    	this.addMetaData(document);
    	this.addTitle(document,"Cesta Comprada por Consumidor");
    	this.addContentCesta(document,idCesta);
    	this.addFooter(document);
    }
    
    /**
     * Incluye metadata al PDF para ser consultada en Adobe
     * @param document
     */
    private void addMetaData(Document document) {
        document.addTitle("AlimentosKM0");
        document.addSubject("Generacion de recibos");
        document.addKeywords("AlimentosKM0, PDF, iText");
        document.addAuthor(ENTIDAD);
        document.addCreator(ENTIDAD);
    }

    /**
     * Incluye una cabecera al documento
     * @param document: documento a completar
     * @param cabecera: valor de la cabecera
     * @throws DocumentException
     */
    private void addTitle(Document document, String cabecera) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(cabecera, catFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "Este documento tiene que ser presentado en las oficinas de comercio del ayuntamiento.", redFont));
        document.add(preface);
    }

    /**
     * Incluye la informacion del paquete al documento
     * @param document: documento a completar
     * @param idPaquete: id del paquete
     * @throws DocumentException
     */
    private void addContentPropuesta(Document document, int idPaquete) throws DocumentException {   
    	Paquete paquete = new Paquete();
    	paquete.buscarID(idPaquete);
    	Productor productor = paquete.getProductor();
    	
    	PdfPTable table = new PdfPTable(2);
    	table.setHeaderRows(2);
    	table.setFooterRows(1);
    	table.setSpacingBefore(20);

    	//cabecera
    	PdfPCell c1 = new PdfPCell(new Phrase("Identificador Propuesta"));
    	c1.setGrayFill(0.7f);
        table.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(Integer.toString(paquete.getId())));
    	c2.setGrayFill(0.7f);
        table.addCell(c2);
        
        //pie
        PdfPCell c3 = new PdfPCell(new Phrase("Precio"));
        c3.setGrayFill(0.7f);
        table.addCell(c3);
        PdfPCell c4 = new PdfPCell(new Phrase(Double.toString(paquete.getPrecio())));
        c4.setGrayFill(0.7f);
        table.addCell(c4);

        //contenido de la tabla
        table.addCell("Productor");
        table.addCell(productor.getNombreCompleto());
        table.addCell("DNI");
        table.addCell(productor.getDni());
        table.addCell("Terreno");
        table.addCell(paquete.getNombreTerreno());
        table.addCell("Alimento");
        table.addCell(paquete.getNombreAlimento());
        table.addCell("Fecha Propuesta");
        table.addCell(paquete.getFechaPropuesta().toString());
        table.addCell("Cantidad Propuesta");
        table.addCell(Double.toString(paquete.getCantidadPropuesta()));
        table.addCell("Medida");
        table.addCell(paquete.getAlimento().getMedida());
        table.addCell("Precio/Medida");
        table.addCell(Double.toString(paquete.getAlimento().getPrecio()));
       
        table.addCell("Fecha Aceptada");
        table.addCell(paquete.getFechaAceptacion().toString());
        table.addCell("Cantidad Aceptada");
        table.addCell(Double.toString(paquete.getCantidadAceptada()));       

        document.add(table);
    }
    
    /**
     * Incluye la informacion de la cesta al documento
     * @param document: documento a completar
     * @param idCesta: id de la cesta
     * @throws DocumentException
     */
    private void addContentCesta(Document document, int idCesta) throws DocumentException {   
    	Cesta cesta = new Cesta();
    	cesta.buscarID(idCesta);
    	ArrayList<Porcion> porciones = (ArrayList<Porcion>)cesta.obtenerPorciones();
    	
    	PdfPTable table = new PdfPTable(2);
    	table.setHeaderRows(2);
    	table.setFooterRows(1);
    	table.setSpacingBefore(20);

    	//cabecera
    	PdfPCell c1 = new PdfPCell(new Phrase("Identificador Cesta"));
    	c1.setGrayFill(0.7f);
        table.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(Integer.toString(idCesta)));
    	c2.setGrayFill(0.7f);
        table.addCell(c2);
        
        //pie
        PdfPCell c3 = new PdfPCell(new Phrase("Precio"));
        c3.setGrayFill(0.7f);
        table.addCell(c3);
        PdfPCell c4 = new PdfPCell(new Phrase(Double.toString(cesta.getPrecio())));
        c4.setGrayFill(0.7f);
        table.addCell(c4);

        //cnontenido
        table.addCell("Nombre Usuario");
        table.addCell(cesta.getUsuarioNombreCompleto());
        table.addCell("Nombre Cesta");
        table.addCell(cesta.getNombre());
        table.addCell("Fecha Compra");
        table.addCell(cesta.getFechaCompra().toString());
        table.addCell("Resumen porciones");
        String resumen = "";
        if (porciones != null) {
	        for(Porcion p: porciones) {
	        	if (!resumen.equals(""))
	        		resumen += " , ";
	        	resumen += p.getNombreAlimento();
	        }
        }
        table.addCell(resumen);
 
        document.add(table);
    }
    
    /**
     * Incluye el pie al documento
     * @param document: documento a completar
     * @throws DocumentException
     */
    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph();
        addEmptyLine(footer, 1);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = format.format(new Date());
        footer.add(new Paragraph("Recibo generado a " + dateString, smallBold));
       
        document.add(footer);
    }

    /**
     * Incluye l?neas en blanco al p?rrafo pasado
     * @param paragraph: p?rrafo
     * @param number: n?mero de l?neas en blanco
     */
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
