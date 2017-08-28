import com.google.common.io.CharStreams;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.resource.ClassPathResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by novikovma on 8/27/2017.
 */
public class HtmlEngine extends TemplateEngine {
    @Override
    public String render(ModelAndView modelAndView) {

        String html;
        try{
            ClassPathResource classPathResource = new ClassPathResource("/public/" + modelAndView.getViewName());
            InputStream stream = classPathResource.getInputStream();
            html = CharStreams.toString(new InputStreamReader(stream , StandardCharsets.UTF_8));
        } catch (Exception e){
            html = "error";
        }
        return html;
    }
}
