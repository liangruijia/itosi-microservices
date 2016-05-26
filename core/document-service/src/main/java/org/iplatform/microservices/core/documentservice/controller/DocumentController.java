package org.iplatform.microservices.core.documentservice.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentListResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentSearchLogDO;
import org.iplatform.microservices.core.documentservice.dao.DocumentMapper;
import org.iplatform.microservices.core.documentservice.properties.DocumentServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhanglei
 * 文档管理，文档上传、下载、信息、在线预览、我的文档列表
 */
@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
	private static final Log logger = LogFactory.getLog(DocumentController.class);
	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private DocumentServiceProperties docServiceProperties;


	/**
	 * @api {post} /document/ 上传文档
	 * @apiGroup Document
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	-F "file=@file.doc" \
	 * 	https://localhost:8000/documentservice/api/v1/document/
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "message": null
	 *   "document": {
	 *   "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *   "file_name": "文档.xlsx",
	 *   "author": "zhanglei",
	 *   "links": [         {
	 *     "id": "view",
	 *     "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *       }]
	 *    }
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestParam(value = "file", required = false) MultipartFile file,@RequestHeader(value="Authorization") String authorizationHeader,Principal principal) {

		DocumentResponse documentrs = new DocumentResponse();
		try {
			String fileName = file.getOriginalFilename();
			Path path = getFileStorePath();

			File targetFile = new File(path.toFile(), fileName+"-"+UUID.randomUUID().toString().replaceAll("-", ""));
			targetFile.createNewFile();
			file.transferTo(targetFile);

			DocumentDO document = new DocumentDO();
			document.setFile_id(UUID.randomUUID().toString().replaceAll("-", ""));
			document.setFile_name(fileName);
			document.setAuthor(principal.getName());
			document.setFile_path(targetFile.getPath());
			documentrs.setDocument(document);

			DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
			documentOpLog.setFile_id(document.getFile_id());
			documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			documentOpLog.setOperater(principal.getName());
			documentOpLog.setOptype("create");
			
			documentMapper.create(document);
			documentMapper.createoplog(documentOpLog);
			return new ResponseEntity<>(documentrs, HttpStatus.CREATED);
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
		
	}

	/**
	 * @api {get} /document/:fileid/download 下载文档
	 * @apiGroup Document
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-o file.docx \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/documentservice/document/81bdcd1a28c948bb881cf3e9a31cd782/download
	 */	
	@RequestMapping(value = "/{fileid}/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("fileid") String fileid,Principal principal)
			throws Exception {
		DocumentDO document = documentMapper.findById(fileid);
		Path filepath = FileSystems.getDefault().getPath(document.realFile_path());
		byte[] bytes = Files.readAllBytes(filepath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", document.getFile_name());
		
		DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
		documentOpLog.setFile_id(document.getFile_id());
		documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		documentOpLog.setOperater(principal.getName());
		documentOpLog.setOptype("download");
		documentMapper.createoplog(documentOpLog);
		
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
	}

	/**
	 * @api {get} /document/:fileid/info 获取文档信息
	 * @apiGroup Document
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *     "file_name": "文档.xlsx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}/info", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> info(@PathVariable("fileid") String fileid,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try{
			DocumentDO document = documentMapper.findById(fileid);	
			if (document != null) {
				documentrs.setDocument(document);
			} else {
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("文件不存在");
			}
			
			DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
			documentOpLog.setFile_id(fileid);
			documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			documentOpLog.setOperater(principal.getName());
			documentOpLog.setOptype("info");
			documentMapper.createoplog(documentOpLog);	
			return new ResponseEntity(documentrs, HttpStatus.OK);
		}catch(Exception e){
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity(documentrs, HttpStatus.BAD_REQUEST);
		}		
	}
	

	/**
	 * @api {delete} /document/:fileid 删除文档
	 * @apiGroup Document
	 * @apiDescription 删除文档会同时删除这个文档的分享
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X DELETE \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 204 NO CONTENT
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782"
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("fileid") String fileid,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			DocumentDO document = documentMapper.findById(fileid);
			document.setFile_id(fileid);
			documentMapper.deleteById(fileid);
			documentMapper.deleteByFilePath(document.realFile_path());
			
			DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
			documentOpLog.setFile_id(fileid);
			documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			documentOpLog.setOperater(principal.getName());
			documentOpLog.setOptype("delete");
			documentMapper.createoplog(documentOpLog);	
			documentrs.setDocument(document);
			return new ResponseEntity<>(documentrs, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * @api {post} /document/:fileid/?moveto_catalog_id=:catalog_id 移动文档到目录
	 * @apiGroup Document
	 * @apiDescription 将这个文档移动到指定的目录，移动后这个文档在原有目录下消失
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X POST \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/?moveto_catalog_id=:catalog_id
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 204 NO CONTENT
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782"
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}/", method = RequestMethod.POST)
	public ResponseEntity<?> moveto(@PathVariable("fileid") String fileid,@RequestParam(value = "moveto_catalog_id") String moveto_catalog_id,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			DocumentDO mydocument = documentMapper.findById(fileid);
			if(mydocument.getAuthor().equals(principal.getName())){
				DocumentDO document = new DocumentDO();
				document.setCatalog_id(moveto_catalog_id);
				documentMapper.update(document);
				documentrs.setDocument(document);
				return new ResponseEntity<>(documentrs, HttpStatus.NO_CONTENT);				
			}else{
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("不是文档的拥有者不可以移动文档");
				return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);				
			}
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
	}	
	
	/**
	 * @api {post} /document/:fileid/?shareto_catalog_id=:catalog_id 分享文档到目录
	 * @apiGroup Document
	 * @apiDescription 分享这个文档的访问地址到指定目录，此文档删除后分享文档也将消失
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X POST \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/?shareto_catalog_id=:catalog_id
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 204 NO CONTENT
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782"
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}/", method = RequestMethod.POST)
	public ResponseEntity<?> shareto(@PathVariable("fileid") String fileid,@RequestParam(value = "shareto_catalog_id") String shareto_catalog_id,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			DocumentDO document = documentMapper.findById(fileid);
			if(document.getAuthor().equals(principal.getName())){
				document.setFile_id(UUID.randomUUID().toString().replaceAll("-", ""));
				document.setCatalog_id(shareto_catalog_id);
				documentMapper.create(document);
				documentrs.setDocument(document);
				return new ResponseEntity<>(documentrs, HttpStatus.NO_CONTENT);				
			}else{
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("不是文档的拥有者不可以分享文档");
				return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);				
			}
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
	}	
	
	/**
	 * @api {post} /document/:fileid/?copyto_catalog_id=:catalog_id 复制文档到目录
	 * @apiGroup Document
	 * @apiDescription 复制文档到指定目录，此时会产生一个文件副本，源文档的修改不会影响副本
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X POST \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/?copyto_catalog_id=:catalog_id
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 204 NO CONTENT
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782"
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}/", method = RequestMethod.POST)
	public ResponseEntity<?> copyto(@PathVariable("fileid") String fileid,@RequestParam(value = "copyto_catalog_id") String copyto_catalog_id,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			DocumentDO document = documentMapper.findById(fileid);
			if(document.getAuthor().equals(principal.getName())){
				String file_name = document.getFile_name();
				String copy_file_name = file_name+"-"+UUID.randomUUID().toString().replaceAll("-", "");
				Path source = Paths.get(document.realFile_path());								
				Path target = Paths.get(source.getParent()+File.separator+copy_file_name);
				
				Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
				
				document.setFile_id(UUID.randomUUID().toString().replaceAll("-", ""));
				document.setCatalog_id(copyto_catalog_id);
				document.setFile_path(target.toString());
				document.setFile_name(file_name);
				
				documentMapper.create(document);
				documentrs.setDocument(document);
				return new ResponseEntity<>(documentrs, HttpStatus.NO_CONTENT);				
			}else{
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("不是文档的拥有者不可以分享文档");
				return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);				
			}
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
	}	

	/**
	 * @api {get} /document/ 获取我的文档列表
	 * @apiGroup Document
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "documents": [{
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *     "file_name": "文档.xlsx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   },{
	 *     "file_id": "646a2f9a7cb34151a8cdfd618aeb3018",
	 *     "file_name": "文档2.docx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download"
	 *     }]
	 *   }]
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> list(Principal principal) {
		DocumentListResponse documentrs = new DocumentListResponse();
		try {
			List<DocumentDO> documents = documentMapper.mylistWithOutCatalog(principal.getName());
			if (documents != null) {
				documentrs.setDocuments(documents);
			}
			return new ResponseEntity<>(documentrs, HttpStatus.OK);
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}	
	}

	/**
	 * @api {get} /document/search 按文件名模糊搜索
	 * @apiGroup Document
	 * @apiParam {String} q 搜索条件，多个条件用加号连接，每个条件采用模糊匹配，多个条件之间采用或的关系
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/search?q=亿阳+指南
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "documents": [{
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *     "file_name": "亿阳企业文化.xlsx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   },{
	 *     "file_id": "646a2f9a7cb34151a8cdfd618aeb3018",
	 *     "file_name": "开发指南.docx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download"
	 *     }]
	 *   }]
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */		
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public DocumentListResponse search(@RequestParam("q") String q,Principal principal) {
		DocumentListResponse documentrs = new DocumentListResponse();
		try {
			if(q!=null && q.trim().length()>0){
				String[] file_names = q.split(" ");
				int index=0;
				for(String file_name : file_names){
					file_names[index++] = "%"+file_name+"%";
				}
				Map<String,Object> params = new HashMap();
				params.put("author", principal.getName());
				params.put("file_names",file_names);
				List<DocumentDO> documents = documentMapper.search(params);
				if (documents != null) {
					documentrs.setDocuments(documents);
				}	
				
				DocumentSearchLogDO documentSearchLog = new DocumentSearchLogDO();
				documentSearchLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
				documentSearchLog.setOperater(principal.getName());
				documentSearchLog.setSearchparam(q);			
				documentMapper.createsearchlog(documentSearchLog);
			}else{
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("查询参数不能为空");
			}
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
		}
		return documentrs;
	}	

	/**
	 * 获取文件存储跟路径
	 * 
	 * @return
	 * @throws IOException
	 */
	private Path getFileStorePath() throws IOException {
		Path path = FileSystems.getDefault().getPath(docServiceProperties.getDocumentpath());
		boolean pathexists = Files.notExists(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
		if (pathexists) {
			path.toFile().mkdir();
		}
		return path;
	}
	
	public static void main(String[] args){
		Path path = Paths.get("/tmp/src/file-19.dat.COMPLETED");
		System.out.println(path.getFileName());
		System.out.println(path.getParent());
		System.out.println(path.getRoot());
	}
}