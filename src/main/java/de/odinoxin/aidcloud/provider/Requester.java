package de.odinoxin.aidcloud.provider;

import de.odinoxin.aiddesk.Login;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class Requester {
    static void setRequestHeaders(Object port) {
        if (port == null || !(port instanceof BindingProvider))
            return;
        Map<String, Object> reqCtx = ((BindingProvider) port).getRequestContext();
        if (reqCtx != null) {
            Map<String, List<String>> reqHeaders = new HashMap<>();
            reqHeaders.put("Username", Login.getPerson() == null ? null : Collections.singletonList(String.valueOf(Login.getPerson().getId())));
            reqHeaders.put("Password", Login.getPerson() == null ? null : Collections.singletonList(Login.getPerson().getPwd()));
            reqCtx.put(MessageContext.HTTP_REQUEST_HEADERS, reqHeaders);
        }
    }
}
