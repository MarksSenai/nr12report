package br.com.nr12.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.nr12.dao.jpa.ClienteJPADAO;
import br.com.nr12.dao.jpa.ConfiguracoesJPADAO;
import br.com.nr12.dao.jpa.LaudoJPADAO;
import br.com.nr12.dao.jpa.MaquinaJPADAO;
import br.com.nr12.dao.jpa.NormaTecnicaJPADAO;
import br.com.nr12.model.Cliente;
import br.com.nr12.model.Configuracoes;
import br.com.nr12.model.Dispositivo;
import br.com.nr12.model.Laudo;
import br.com.nr12.model.Maquina;
import br.com.nr12.model.NormaTecnica;
import br.com.nr12.model.PontoPerigo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PdfService extends UtilService{
	
	private String saida; 
	
	public PdfService(HttpServletRequest request){
		super.request = request;
	}
	
	public String geraRelatorio(int idLaudo){
		
		saida = null;
		
		String pagina1 = getDiretorioReal("/report/Pagina1.jasper");
		String pagina2 = getDiretorioReal("/report/Pagina2.jasper");
		String pagina3 = getDiretorioReal("/report/Pagina3.jasper");
		String pagina4 = getDiretorioReal("/report/Pagina4.jasper");
		String pagina5 = getDiretorioReal("/report/Pagina5.jasper");
		String pagina6 = getDiretorioReal("/report/Pagina6.jasper");
		String pagina7 = getDiretorioReal("/report/Pagina7.jasper");
		String pagina8 = getDiretorioReal("/report/Pagina8.jasper");
		String pagina9 = getDiretorioReal("/report/Pagina9.jasper");
		String pagina10 = getDiretorioReal("/report/Pagina10.jasper");
		String pagina11 = getDiretorioReal("/report/Pagina11.jasper");
		
		ArrayList<Laudo> laudo = buscarPrimeiraPagina(idLaudo);
		
		String laudoPdf = "/pdf/"+laudo.get(0).getCodigo()+".pdf";
		
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			
			// pagina1 - Titulos
			
			map.put("laudo", laudo.get(0));
			map.put("maquina", laudo.get(0).getMaquina().getNome());
			System.out.println(laudo.get(0).getMaquina().getTipoMaquina().getNome());
			JRBeanCollectionDataSource ds1 = new JRBeanCollectionDataSource(laudo);
			JasperPrint print1 = JasperFillManager.fillReport(pagina1, map, ds1);
			preencherPdf(print1, 1);
			
			// pagina2 - 1. Portaria Vigente
			ArrayList<Configuracoes> config = buscarConfiguracoes();
			JRBeanCollectionDataSource ds2 = new JRBeanCollectionDataSource(config);
			JasperPrint print2 = JasperFillManager.fillReport(pagina2, map, ds2);
			preencherPdf(print2, 2);
			
			// pagina3 - 2. Identificação da Empresa
			//ArrayList<Cliente> cliente = buscarCliente(idLaudo);
			JRBeanCollectionDataSource ds3 = new JRBeanCollectionDataSource(laudo);
			JasperPrint print3 = JasperFillManager.fillReport(pagina3, map, ds3);
			preencherPdf(print3, 3);
			
			// pagina4 - 3. Identificação da máquina
			//ArrayList<Maquina> maquina = buscarMaquina(idLaudo);
			JRBeanCollectionDataSource ds4 = new JRBeanCollectionDataSource(laudo);
			JasperPrint print4 = JasperFillManager.fillReport(pagina4, map, ds4);
			preencherPdf(print4, 4);
			
			
			// pagina  - Limites da Máquina
			
			
			
			// pagina5 - 4. Pontos de perigo e sistemas de segurança
			
			List<PontoPerigo> pontosPerigo = buscarPontosPerigo(idLaudo);
			JRBeanCollectionDataSource ds5 = new JRBeanCollectionDataSource(pontosPerigo);
			JasperPrint print5 = JasperFillManager.fillReport(pagina5, map, ds5);
			preencherPdf(print5, 5);
						
			// pagina6 - 5. Dispositivos de partida, acionamento e parada
			map.put("dispositivos", config.get(0).getDispositivos());
			List<Dispositivo> dispositivos = buscarDispositivos(idLaudo);
			JRBeanCollectionDataSource ds6 = new JRBeanCollectionDataSource(dispositivos);
			JasperPrint print6 = JasperFillManager.fillReport(pagina6, map, ds6);
			preencherPdf(print6, 6);
			
			// pagina7 - 6. Capacitação
			JRBeanCollectionDataSource ds7 = new JRBeanCollectionDataSource(laudo);
			JasperPrint print7 = JasperFillManager.fillReport(pagina7, map, ds7);
			preencherPdf(print7, 7);
			
			// pagina8 - 7. Normas técnicas aplicadas
			//List<NormaTecnica> normaTecnica = buscarNormaTecnicaLaudo(idLaudo);
			JRBeanCollectionDataSource ds8 = new JRBeanCollectionDataSource(laudo);
			JasperPrint print8 = JasperFillManager.fillReport(pagina8, map, ds8);
			preencherPdf(print8, 8);
			
			// pagina9 - 8. Disposições Finais
			JRBeanCollectionDataSource ds9 = new JRBeanCollectionDataSource(config);
			JasperPrint print9 = JasperFillManager.fillReport(pagina9, map, ds9);
			preencherPdf(print9, 9);
			
			// pagina10 - 9. Responsabilidade técnica
			JRBeanCollectionDataSource ds10 = new JRBeanCollectionDataSource(config);
			JasperPrint print10 = JasperFillManager.fillReport(pagina10, map, ds10);
			preencherPdf(print10, 10);
			
			// pagina10 - 9. Responsabilidade técnica
			JRBeanCollectionDataSource ds11 = new JRBeanCollectionDataSource(laudo);
			JasperPrint print11 = JasperFillManager.fillReport(pagina11, map, ds11);
			preencherPdf(print11, 11);
						
			/*
			List<InputStream> inputPdfList = new ArrayList<InputStream>();
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina1.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina2.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina3.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina4.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina11.pdf")));
			
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina5.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina6.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina7.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina8.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina9.pdf")));
			inputPdfList.add(new FileInputStream(getDiretorioReal("/pdf/pagina10.pdf")));
			
			OutputStream outputStream = new FileOutputStream(getDiretorioReal(laudoPdf));
			
			mergePdfFiles(inputPdfList, outputStream);

			*/
			Document document = new Document();
			PdfCopy merge = new PdfCopy(document, new FileOutputStream(getDiretorioReal(laudoPdf)));
			merge.setMergeFields();
			document.open();
			PdfReader pag1 = new PdfReader(getDiretorioReal("/pdf/pagina1.pdf"));
			merge.addDocument(pag1);
			PdfReader pag2 = new PdfReader(getDiretorioReal("/pdf/pagina2.pdf"));
			merge.addDocument(pag2);
			PdfReader pag3 = new PdfReader(getDiretorioReal("/pdf/pagina3.pdf"));
			merge.addDocument(pag3);
			PdfReader pag4 = new PdfReader(getDiretorioReal("/pdf/pagina4.pdf"));
			merge.addDocument(pag4);
			PdfReader pag11 = new PdfReader(getDiretorioReal("/pdf/pagina11.pdf"));
			merge.addDocument(pag11);
			PdfReader pag5 = new PdfReader(getDiretorioReal("/pdf/pagina5.pdf"));
			merge.addDocument(pag5);
			PdfReader pag6 = new PdfReader(getDiretorioReal("/pdf/pagina6.pdf"));
			merge.addDocument(pag6);
			PdfReader pag7 = new PdfReader(getDiretorioReal("/pdf/pagina7.pdf"));
			merge.addDocument(pag7);
			PdfReader pag8 = new PdfReader(getDiretorioReal("/pdf/pagina8.pdf"));
			merge.addDocument(pag8);
			PdfReader pag9 = new PdfReader(getDiretorioReal("/pdf/pagina9.pdf"));
			merge.addDocument(pag9);
			PdfReader pag10 = new PdfReader(getDiretorioReal("/pdf/pagina10.pdf"));
			merge.addDocument(pag10);
			document.close();
			pag1.close();
			pag2.close();
			pag3.close();
			pag4.close();
			pag11.close();
			pag5.close();
			pag6.close();
			pag7.close();
			pag8.close();
			pag9.close();
			pag10.close();
			
			saida = getContextPath() + laudoPdf;
			//saida = getContextPath() + "/pdf/pagina5.pdf";
			
			//saida = "NR12" + laudoPdf;
			System.out.println("saida: " + saida);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return saida;
	}
	
	static void mergePdfFiles(List<InputStream> inputPdfList,
	        OutputStream outputStream) throws Exception{
	    //Create document and pdfReader objects.
	    Document document = new Document();
	    List<PdfReader> readers = 
	            new ArrayList<PdfReader>();
	    int totalPages = 0;

	    //Create pdf Iterator object using inputPdfList.
	    Iterator<InputStream> pdfIterator = inputPdfList.iterator();

	    // Create reader list for the input pdf files.
	    while (pdfIterator.hasNext()) {
	            InputStream pdf = pdfIterator.next();
	            PdfReader pdfReader = new PdfReader(pdf);
	            readers.add(pdfReader);
	            totalPages = totalPages + pdfReader.getNumberOfPages();
	    }

	    // Create writer for the outputStream
	    PdfWriter writer = PdfWriter.getInstance(document, outputStream);

	    //Open document.
	    document.open();

	    //Contain the pdf data.
	    PdfContentByte pageContentByte = writer.getDirectContent();

	    PdfImportedPage pdfImportedPage;
	    int currentPdfReaderPage = 1;
	    Iterator<PdfReader> iteratorPDFReader = readers.iterator();

	    // Iterate and process the reader list.
	    while (iteratorPDFReader.hasNext()) {
	            PdfReader pdfReader = iteratorPDFReader.next();
	            //Create page and add content.
	            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
	                  document.newPage();
	                  pdfImportedPage = writer.getImportedPage(pdfReader,currentPdfReaderPage);
	                  pageContentByte.addTemplate(pdfImportedPage, 0, 0);
	                  currentPdfReaderPage++;
	            }
	            currentPdfReaderPage = 1;
	    }

	    //Close document and outputStream.
	    outputStream.flush();
	    document.close();
	    outputStream.close();

	    System.out.println("Pdf files merged successfully.");
	}
	
	private List<PontoPerigo> buscarPontosPerigo(int idLaudo) {
		List<PontoPerigo> array = null;
		LaudoJPADAO dao = new LaudoJPADAO();
		array = dao.buscarPontosPerigo(idLaudo);
		return array;
	}
	
	private List<Dispositivo> buscarDispositivos(int idLaudo) {
		List<Dispositivo> array = null;
		LaudoJPADAO dao = new LaudoJPADAO();
		array = dao.buscarDispositivos(idLaudo);
		return array;
	}

	private void preencherPdf(JasperPrint print, int numPag) throws JRException{
		String path = "/pdf/pagina"+numPag+".pdf";
		String caminho = getDiretorioReal(path);
		JasperExportManager.exportReportToPdfFile(print, caminho);
		caminho = getContextPath() + path;
		System.out.println("PDF: " + caminho);
	}
	
	private ArrayList<Laudo> buscarPrimeiraPagina(int idLaudo) {
		ArrayList<Laudo> arrayLaudo = new ArrayList<Laudo>();
		Laudo laudo = new Laudo();
		LaudoJPADAO dao = new LaudoJPADAO();
		//laudo = dao.buscarPrimeiraPagina(idLaudo);
		laudo = dao.buscarPrimeiraPagina(idLaudo);
		arrayLaudo.add(laudo);
		return arrayLaudo;
	}
	
	private ArrayList<Configuracoes> buscarConfiguracoes() {
		ArrayList<Configuracoes> arrayCfg = new ArrayList<Configuracoes>();
		Configuracoes cfg = new Configuracoes();
		ConfiguracoesJPADAO dao = new ConfiguracoesJPADAO();
		cfg = dao.buscarConfiguracoes();
		arrayCfg.add(cfg);
		return arrayCfg;
	}
	
	private ArrayList<Cliente> buscarCliente(int idLaudo) {
		ArrayList<Cliente> arrayCliente = new ArrayList<Cliente>();
		Cliente cliente = new Cliente();
		ClienteJPADAO dao = new ClienteJPADAO();
		cliente = dao.buscarCliente(idLaudo);
		arrayCliente.add(cliente);
		return arrayCliente;
	}
	
	private List<NormaTecnica> buscarNormaTecnicaLaudo(int idLaudo) {
		List<NormaTecnica> arrayNormaTecnica = new ArrayList<NormaTecnica>();
		NormaTecnicaJPADAO dao = new NormaTecnicaJPADAO();
		arrayNormaTecnica = dao.buscarNormaTecnicaLaudo(idLaudo);
		return arrayNormaTecnica;
	}
	
	private ArrayList<Maquina> buscarMaquina(int idLaudo) {
		ArrayList<Maquina> arrayMaquina = new ArrayList<Maquina>();
		Maquina maquina = new Maquina();
		MaquinaJPADAO dao = new MaquinaJPADAO();
		maquina = dao.buscarMaquina(idLaudo);
		arrayMaquina.add(maquina);
		return arrayMaquina;
	}	
}
