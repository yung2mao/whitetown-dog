package cn.whitetown.dogbase.viewmodule;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author GrainRain
 * @date 2020/05/31 16:22
 **/
public class WhiteWebRender implements WebRenderExt {
    @Override
    public void modify(Template template, GroupTemplate groupTemplate, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        template.binding("base","static");
    }
}
