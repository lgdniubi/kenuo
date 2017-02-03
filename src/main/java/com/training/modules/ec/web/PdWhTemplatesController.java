package com.training.modules.ec.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.web.BaseController;

/**
 * 仓库模板
 * 
 * @author dalong
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/pdWhTemplates")
public class PdWhTemplatesController extends BaseController {
}
