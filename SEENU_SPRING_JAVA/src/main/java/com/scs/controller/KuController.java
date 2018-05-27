
package com.scs.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.entity.model.Ku;
import com.scs.entity.model.Response;
import com.scs.exception.ApiException;
import com.scs.model.BCSettingsModel;
import com.scs.model.BaseRequestModel;
import com.scs.service.WorkFlowDbServices;
import com.scs.service.impl.ImportDbServicesImpl;
import com.scs.service.impl.KuDbServicesImpl;
import com.scs.service.impl.WorkFlowDbServicesImpl;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiConstants.API)
// @CrossOrigin(origins = {"http://localhost:4200",
// "http://localhost:4300","http://10.10.10.212:4200"})
@MultipartConfig(fileSizeThreshold = 20971520)
public class KuController {

	private static final Logger logger = Logger.getLogger(KuController.class);
	private static final String CONTROLLER_END_EXCEPTION = "KU CONTROLLER  ENDED WITH EXCEPTION";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private KuDbServicesImpl kuDbService;

	@Autowired
	private WorkFlowDbServicesImpl WorkFlowDbService;
	
	@Autowired
	private ImportDbServicesImpl importDbService;

	@Autowired
	private WorkFlowDbServices flowChartDbServices;

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_KU_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getku(@RequestParam("kuLstType") String kuLstType, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {
			List<Ku> kuLst;
			kuLst = (List<Ku>) kuDbService.getKuDetails(baseModel);
			if (kuLstType.equals(ApiConstants.KU_DROPDOWN_CODE)) {
				for (Ku ku : kuLst) {
					ku.setEntities(null);
					ku.setIntents(null);
				}
			}
			responseObject.put(ApiConstants.API_RESPONSE, kuLst);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getkuById(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			Ku ku;
			ku = (Ku) kuDbService.getKuById(baseModel, id);

			responseObject.put(ApiConstants.API_RESPONSE, ku);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	
	
	
	@PostMapping(value = ApiConstants.KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object connect(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object response = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			response = kuDbService.createKU(baseModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return response;
	}

	@DeleteMapping(value = ApiConstants.KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object delete(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			WorkFlowDbService.deleteWorkflowbyKuId(id);

			kuDbService.deleteKU(baseModel, id);

			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PutMapping(value = ApiConstants.KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			kuDbService.updateKU(baseModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put("response", "success");
	}

	

	@RequestMapping(value = "/importFiles", method = RequestMethod.POST)
	public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files,
			@RequestParam("file1") MultipartFile[] file1, @RequestParam("fname") String firstName,
			@RequestParam("lname") String lastName) {
		String fileName = null;
		String msg = "";
		String lineBreak = "<br/>";

		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				try {
					fileName = files[i].getOriginalFilename();
					byte[] bytes = files[i].getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(new File("C:/Passport/" + fileName)));
					buffStream.write(bytes);
					buffStream.close();
					msg += "You have successfully uploaded " + fileName + lineBreak;
				} catch (Exception e) {
					logger.error(Utility.getExceptionMessage(e));
					return "You failed to upload " + fileName + ": " + e.getMessage() + lineBreak;
				}
			}

		}

		else {
			return "Unable to upload. File is empty.";
		}

		if (file1 != null && file1.length > 0) {
			for (int i = 0; i < file1.length; i++) {
				try {
					fileName = file1[i].getOriginalFilename();
					byte[] bytes = file1[i].getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(new File("C:/Visa/" + fileName)));
					buffStream.write(bytes);
					buffStream.close();
					msg += "You have successfully uploaded " + fileName + lineBreak;
				} catch (Exception e) {
					logger.error(Utility.getExceptionMessage(e));
					return "You failed to upload " + fileName + ": " + e.getMessage() + lineBreak;
				}
			}
			return msg;
		} else {
			return "Unable to upload. File is empty.";
		}

	}

	

	@PostMapping(value = "/readFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object importReadFile(@RequestParam("file") MultipartFile multiparFile, BaseRequestModel baseModel,
			HttpSession session) throws ApiException {

		Ku ku = null;

		try {

			ObjectMapper mapper = new ObjectMapper();

			ku = mapper.readValue(multiparFile.getInputStream(), Ku.class);

			kuDbService.checkNames(baseModel, ku);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return ku;
	}

	// VALIDATE_IMPORT
	@PostMapping(value = ApiConstants.VALIDATE_IMPORT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object validateImportKu(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Ku kus = null;
		
		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			kus = (Ku) kuDbService.checkNames(baseModel, baseModel.getKus());

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return kus;
	}

	@PostMapping(value = ApiConstants.KU_IMPORT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createImportKu(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object response = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			response = importDbService.importNewKu(baseModel.getKus());

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return response;
	}

	@PostMapping(value = ApiConstants.SETTINGS_IMPORT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createImportSettings(@RequestParam("file") MultipartFile multiparFile, BaseRequestModel baseModel,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			ObjectMapper mapper = new ObjectMapper();

			BCSettingsModel bcSettingsModel = mapper.readValue(multiparFile.getInputStream(), BCSettingsModel.class);
			kuDbService.importSettings(bcSettingsModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put("response", ApiConstants.SUCCESS);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.SETTINGS_KEYWORDS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getSettings(BaseRequestModel baseModel) throws ApiException {
		HashMap<String, Object> responseObject = new HashMap<>();
		BCSettingsModel setLst;

		try {

			setLst = (BCSettingsModel) kuDbService.getSettings(baseModel);
			responseObject.put(ApiConstants.API_RESPONSE, setLst);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

}
