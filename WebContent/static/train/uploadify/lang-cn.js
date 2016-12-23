/**
* uploadify 中文语言包
* 需修改uploadify.js原文件的内容
* 如若要压缩，请自行对原文件进行压缩处理
* @author 楓之落葉
* @mail feng_luoye@foxmail.com
*/
var $lang={
	errorMsgPrefix : "文件不能添加到队列中:",		//信息提示前缀
	QUEUE_LIMIT_EXCEEDED_NUMBER:"选定的文件数量超过剩余的上传限制({0}).",		//文件个数超过限制 {0} 为文件个数参数
	QUEUE_LIMIT_EXCEEDED_SIZE:"选定的文件总大小超过了队列的大小限制({0}).",			//文件的总大小超过限制  {0}文件总大小
	FILE_EXCEEDS_SIZE_LIMIT:"文件{0}超过单文件限制({1})",		//单文件大小限制
	ZERO_BYTE_FILE:"文件大小为0",			//文件大小为0
	INVALID_FILETYPE:"文件格式不对,仅限:{0}",
	HTTP_ERROR:"HTTP 错误\n{0}",			//HTT错误
	MISSING_UPLOAD_URL:"上传路径失效.",//
	IO_ERROR:"IO错误.",
	SECURITY_ERROR:"安全性错误.",
	UPLOAD_LIMIT_EXCEEDED:"每次最多上传{0}个.",
	UPLOAD_FAILED:"上传失败.",
	SPECIFIED_FILE_ID_NOT_FOUND:"找不到指定文件.",
	FILE_VALIDATION_FAILED:"参数错误",
	FILE_CANCELLED:"取消文件",
	UPLOAD_STOPPED:"停止上传",
	FILE_ALERADY_EXISTS_SERVER:"文件{0}已存在于服务端,您想替换掉服务器端的文件吗？",
	FILE_ALERADY_EXISTS_QUEUE:"文件{0}已存在于上传队列中,您想替换掉服务器端的文件吗？",
    METHOD_NOT_FOUND:"在$.uploadify中，方法（{0}）未找到。"
};