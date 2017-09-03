package huxg.framework.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import huxg.framework.plugin.fileupload.TFile;

/**
 * 获取Excel文件中的内容列表
 * 
 * @author chengh
 * 
 */
public class ExcelUtils {

	public static List<String> getExcelContent(TFile file) {

		List<String> clist = new ArrayList<String>();

		String filePath = FileUtils.getWebFileAbstractPath(file.getCWjljXd());
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(fin);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row = null;
			int totalRow = sheet.getLastRowNum();
			for (int i = 1; i < totalRow; i++) {
				row = sheet.getRow(i);
				if (row.getCell(0) != null) {
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					String content = row.getCell(0).getStringCellValue().trim();
					clist.add(content);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return clist;
	}
}
