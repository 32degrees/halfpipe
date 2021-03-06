package halfpipe.web;

import lombok.Data;

/**
 * User: spencergibb
 * Date: 4/22/14
 * Time: 10:06 AM
 */
@Data
public class EndpointProperties {
    private String id;
    private boolean enabled = true;
    private boolean sensitive = false;
}
