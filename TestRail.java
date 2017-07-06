package selenium.helpers;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by onur.yenici on 11.04.2017.
 */
public class TestRail {

    private Map testResultData = new HashMap();
    private APIClient client = null;

    public TestRail() {
        testResultData.put("assignedto_id", "2");

        client = new APIClient("https://company.testrail.com/");
        client.setUser("automation@company.com");
        client.setPassword("smJwD5DUWLPB29U*");

    }

    public void setCaseResult(String[] caseIds, String run, boolean isResultFail) throws Exception {

        if (isResultFail) {
            testResultData.put("status_id", "5");
        } else
            testResultData.put("status_id", "1");


        for (int i = 0; i < caseIds.length; i++)
            client.sendPost("add_result_for_case/" + run + "/" + caseIds[i], testResultData);



    }

    public void setJira(File f, String[] caseIds) throws Exception {

        for (int i = 0; i < caseIds.length; i++) {

            JSONObject caseInfo = (JSONObject) client.sendGet("get_case/" + caseIds[i]);

            String jiraTaskId = caseInfo.get("refs").toString();

            File newName = new File(f.getParent() + "/" + caseInfo.get("title").toString() + "_" + new SimpleDateFormat("dd.MM.yy-HH.mm.ss").format(Calendar.getInstance().getTime()) + ".png");
            f.renameTo(newName);
            f = newName;

            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpPost postRequest = new HttpPost("https://domain/rest/api/2/issue/" + jiraTaskId + "/attachments");
            BASE64Encoder base = new BASE64Encoder();
            String encoding = base.encode("name:Autoomasyon1928!".getBytes());
            postRequest.setHeader("Authorization", "Basic " + encoding);
            postRequest.setHeader("X-Atlassian-Token", "nocheck");
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.addPart("file", new FileBody(f));
            postRequest.setEntity(entity.build());
            httpClient.execute(postRequest);
        }

    }

}
