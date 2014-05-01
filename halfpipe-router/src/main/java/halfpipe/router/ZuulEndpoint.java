package halfpipe.router;

import com.netflix.zuul.http.ZuulServlet;
import halfpipe.web.AbstractServletWrappingController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: spencergibb
 * Date: 4/24/14
 * Time: 9:12 PM
 */
@Controller
public class ZuulEndpoint extends AbstractServletWrappingController {

    public ZuulEndpoint() {
        super(ZuulServlet.class, "zuulServlet");
    }

    @RequestMapping("/**")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.controller.handleRequest(request, response);
    }
}
