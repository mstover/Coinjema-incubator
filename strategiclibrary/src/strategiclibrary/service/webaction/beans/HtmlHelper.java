package strategiclibrary.service.webaction.beans;

public class HtmlHelper {

	public String addQueryParam(String url,String param)
	{
		String retUrl = "";
		if(url != null && url.length() > 0)
		{
			if(url.contains("?"))
			{
				if(url.endsWith("&"))
					retUrl = url + param;
				else retUrl = url + "&" + param;
			}
			else retUrl = url + "?" + param;
		}
		else retUrl = "?" + param;
		return retUrl;
	}
}
