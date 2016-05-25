define({ "api": [  {    "type": "delete",    "url": "/catalog/:catalog_id",    "title": "删除目录",    "group": "Catalog",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_id",            "description": "<p>目录ID</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure -i -X DELETE \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true\n  \"catalog\": {\n  \t\"catalog_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\"\n  }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "DeleteCatalogCatalog_id"  },  {    "type": "get",    "url": "/catalog/",    "title": "获取我的根目录下目录和文档",    "group": "Catalog",    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/catalog/",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"catalogChilds\": [\n      {\n          \"catalog_id\": \"d3b2ea4082724b23bbb9896506626e13\",\n          \"parent_catalog_id\": null,\n          \"catalog_name\": \"目录名\",\n          \"isroot\": true,\n          \"author\": \"admin\",\n          \"opdatetime\": \"2016-05-25\"\n      }\n   ],\n   documentChilds: [\n      {\n          \"file_id\": \"75eec2d14e3d41b58a0e35920c4c9615\",\n          \"catalog_id\": null,\n          \"file_name\": \"文件名\",\n          \"author\": \"admin\",\n          \"opdatetime\": \"2016-05-25\"\n      }\n  ]\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "GetCatalog"  },  {    "type": "get",    "url": "/catalog/:catalog_id",    "title": "获取子目录列表",    "group": "Catalog",    "permission": [      {        "name": "none"      }    ],    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_id",            "description": "<p>目录ID</p>"          }        ]      }    },    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"catalogChilds\": [\n      {\n          \"catalog_id\": \"d3b2ea4082724b23bbb9896506626e13\",\n          \"parent_catalog_id\": null,\n          \"catalog_name\": \"目录名\",\n          \"isroot\": true,\n          \"author\": \"admin\",\n          \"opdatetime\": \"2016-05-25\"\n      }\n   ],\n   documentChilds: [\n      {\n          \"file_id\": \"75eec2d14e3d41b58a0e35920c4c9615\",\n          \"catalog_id\": null,\n          \"file_name\": \"文件名\",\n          \"author\": \"admin\",\n          \"opdatetime\": \"2016-05-25\"\n      }\n  ]\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "GetCatalogCatalog_id"  },  {    "type": "patch",    "url": "/catalog/:catalog_id/?catalog_name=新目录名",    "title": "修改目录名称",    "group": "Catalog",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_id",            "description": "<p>目录ID</p>"          },          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_name",            "description": "<p>目录名称</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure -i -X DELETE \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782/?catalog_name=新目录名",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"catalog\": {\n  \t\"catalog_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n   \t\"parent_catalog_id\": null,\n  \t\"catalog_name\": \"目录名\",\n  \t\"author\": \"zhanglei\"\n  }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "PatchCatalogCatalog_idCatalog_name"  },  {    "type": "post",    "url": "/catalog/:catalog_id/?catalog_name=目录名",    "title": "创建子目录",    "group": "Catalog",    "permission": [      {        "name": "none"      }    ],    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_id",            "description": "<p>上级目录ID</p>"          },          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_name",            "description": "<p>目录名称</p>"          }        ]      }    },    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782/?catalog_name=目录名",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"catalog\": {\n  \t\"catalog_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n   \t\"parent_catalog_id\": \"033b04027ae2429f81f985f9cce2978c\",\n  \t\"catalog_name\": \"子目录名\",\n  \t\"author\": \"zhanglei\"\n  }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "PostCatalogCatalog_idCatalog_name"  },  {    "type": "post",    "url": "/catalog/:catalog_id/document/",    "title": "指定目录上传文档",    "group": "Catalog",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_id",            "description": "<p>目录ID</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-X POST\n\t-H \"Authorization: Bearer <access_token>\" \\\n\t-F \"file=@file.doc\" \\\n\thttps://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782/document/",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"message\": null\n  \"document\": {\n  \"file_id\": \"56bdcd1a28c948bb881cf3e9a31cd782\",\n  \"catalog_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n  \"file_name\": \"文档.xlsx\",\n  \"author\": \"zhanglei\",\n  \"links\": [         {\n    \"id\": \"view\",\n    \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download\"\n      }]\n   }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "PostCatalogCatalog_idDocument"  },  {    "type": "post",    "url": "/catalog/?catalog_name=目录名",    "title": "创建我的根目录",    "group": "Catalog",    "permission": [      {        "name": "none"      }    ],    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "catalog_name",            "description": "<p>目录名称</p>"          }        ]      }    },    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/catalog/?catalog_name=目录名",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"catalog\": {\n  \t\"catalog_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n  \t\"catalog_name\": \"目录名\",\n  \t\"author\": \"zhanglei\"\n  }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/CatalogController.java",    "groupTitle": "Catalog",    "name": "PostCatalogCatalog_name"  },  {    "type": "delete",    "url": "/document/:fileid",    "title": "删除文档",    "group": "Document",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "fileid",            "description": "<p>文档ID</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure -i -X DELETE \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 204 NO CONTENT\n{\n  \"success\": true,\n  \"message\": null,\n  \"document\": {\n    \"file_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\"\n  }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 404 Not Found\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/DocumentController.java",    "groupTitle": "Document",    "name": "DeleteDocumentFileid"  },  {    "type": "get",    "url": "/document/",    "title": "获取我的文档列表",    "group": "Document",    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure -i \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/document",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 200 OK\n{\n  \"success\": true,\n  \"message\": null,\n  \"documents\": [{\n    \"file_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n    \"file_name\": \"文档.xlsx\",\n    \"author\": \"zhanglei\",\n    \"links\": [{\n      \"id\": \"view\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download\"\n    }]\n  },{\n    \"file_id\": \"646a2f9a7cb34151a8cdfd618aeb3018\",\n    \"file_name\": \"文档2.docx\",\n    \"author\": \"zhanglei\",\n    \"links\": [{\n      \"id\": \"view\",\n      \"url\": \"/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download\"\n    }]\n  }]\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/DocumentController.java",    "groupTitle": "Document",    "name": "GetDocument"  },  {    "type": "get",    "url": "/document/:fileid/download",    "title": "下载文档",    "group": "Document",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "fileid",            "description": "<p>文档ID</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-o file.docx \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download",        "type": "curl"      }    ],    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/DocumentController.java",    "groupTitle": "Document",    "name": "GetDocumentFileidDownload"  },  {    "type": "get",    "url": "/document/:fileid/info",    "title": "获取文档信息",    "group": "Document",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "fileid",            "description": "<p>文档ID</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure -i \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 200 OK\n{\n  \"success\": true,\n  \"message\": null,\n  \"document\": {\n    \"file_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n    \"file_name\": \"文档.xlsx\",\n    \"author\": \"zhanglei\",\n    \"links\": [{\n      \"id\": \"view\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download\"\n    }]\n  }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/DocumentController.java",    "groupTitle": "Document",    "name": "GetDocumentFileidInfo"  },  {    "type": "get",    "url": "/document/search",    "title": "按文件名模糊搜索",    "group": "Document",    "parameter": {      "fields": {        "Parameter": [          {            "group": "Parameter",            "type": "String",            "optional": false,            "field": "q",            "description": "<p>搜索条件，多个条件用加号连接，每个条件采用模糊匹配，多个条件之间采用或的关系</p>"          }        ]      }    },    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure -i \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\thttps://localhost:8000/api/v1/document/search?q=亿阳+指南",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 200 OK\n{\n  \"success\": true,\n  \"message\": null,\n  \"documents\": [{\n    \"file_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n    \"file_name\": \"亿阳企业文化.xlsx\",\n    \"author\": \"zhanglei\",\n    \"links\": [{\n      \"id\": \"view\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download\"\n    }]\n  },{\n    \"file_id\": \"646a2f9a7cb34151a8cdfd618aeb3018\",\n    \"file_name\": \"开发指南.docx\",\n    \"author\": \"zhanglei\",\n    \"links\": [{\n      \"id\": \"view\",\n      \"url\": \"/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download\"\n    }]\n  }]\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/DocumentController.java",    "groupTitle": "Document",    "name": "GetDocumentSearch"  },  {    "type": "post",    "url": "/document/",    "title": "上传文档",    "group": "Document",    "permission": [      {        "name": "none"      }    ],    "examples": [      {        "title": "Example usage:",        "content": "curl --insecure \\\n\t-H \"Authorization: Bearer <access_token>\" \\\n\t-F \"file=@file.doc\" \\\n\thttps://localhost:8000/api/v1/document/",        "type": "curl"      }    ],    "success": {      "examples": [        {          "title": "Success-Response:",          "content": "HTTP/1.1 201 OK\n{\n  \"success\": true,\n  \"message\": null\n  \"document\": {\n  \"file_id\": \"81bdcd1a28c948bb881cf3e9a31cd782\",\n  \"file_name\": \"文档.xlsx\",\n  \"author\": \"zhanglei\",\n  \"links\": [         {\n    \"id\": \"view\",\n    \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view\"\n    },{\n      \"id\": \"info\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info\"\n    },{\n      \"id\": \"download\",\n      \"url\": \"/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download\"\n      }]\n   }\n}",          "type": "json"        }      ]    },    "error": {      "examples": [        {          "title": "Error-Response:",          "content": "HTTP/1.1 400 Bad Request\n{\n  \"success\": false,\n  \"message\": \"错误信息\"\n}",          "type": "json"        }      ]    },    "version": "0.0.0",    "filename": "src/main/java/org/iplatform/microservices/core/documentservice/controller/DocumentController.java",    "groupTitle": "Document",    "name": "PostDocument"  }] });
