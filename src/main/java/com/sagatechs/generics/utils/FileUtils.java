package com.sagatechs.generics.utils;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;


@Named
@ApplicationScoped
public class FileUtils {

	public void saveFile(byte[] byteArray, String path) throws GeneralAppException {
		File file = new File(path);
		try {
			org.apache.commons.io.FileUtils.writeByteArrayToFile(file, byteArray);
		} catch (IOException e) {
			throw new GeneralAppException("No se pudo guardar el archivo en el servidor",
					Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}

	@SuppressWarnings("unused")
	public File getFile(String path) throws GeneralAppException {
		if (StringUtils.isBlank(path)) {
			throw new GeneralAppException("Dirección de archivo inválida");
		}

		return new File(path);

	}

}
