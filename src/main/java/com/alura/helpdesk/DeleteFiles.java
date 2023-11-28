package com.alura.helpdesk;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public class DeleteFiles {

	public static void main(String[] args) {
		File diretorio = new File("c://exports");

		File[] arquivos = diretorio.listFiles();
		Arrays.sort(arquivos, new ComparatorFile());

		GregorianCalendar c = new GregorianCalendar();
		c.add(Calendar.DAY_OF_MONTH, -180);

		Date dataLimite = c.getTime();
		Date dataArquivoMaisAntigo = new Date(arquivos[arquivos.length - 1].lastModified());

		System.out.println(dataArquivoMaisAntigo.before(dataLimite));
		if (dataArquivoMaisAntigo.before(dataLimite)) {
			for (File file : arquivos) {
				Date dataArquivo = new Date(file.lastModified());

				System.out.println("Nome arquivo " + file.getName() + " data: " + file.lastModified());
				if (dataArquivo.before(dataLimite)) {
					file.delete();
				}
			}
		}

	}

	private static class ComparatorFile implements Comparator<File> {

		public int compare(File o1, File o2) {
			Long data1 = new Long(o1.lastModified());
			Long data2 = new Long(o2.lastModified());
			return data1.compareTo(data2);
		}

	}

}
