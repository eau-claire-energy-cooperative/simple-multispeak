package com.ecec.rweber.multispeak.cb;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.Element;

import com.ecec.rweber.multispeak.MultiSpeak;
import com.ecec.rweber.multispeak.MultiSpeakEndpoint;
import com.ecec.rweber.multispeak.MultiSpeakService;

public class CustomerBilling extends MultiSpeakService {

	public CustomerBilling(MultiSpeakEndpoint endpoint) {
		super(endpoint);
		
	}

	public void getMeterByNo(String meterNum){
		
		//create the request
		Map<String,String> params = new HashMap<String,String>();
		params.put("meterNo",meterNum);
		
		Element result = MultiSpeak.getResult(g_client.sendRequest("GetMeterByMeterNo", params),"GetMeterByMeterNo");
	}
}
