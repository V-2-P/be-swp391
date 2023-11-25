package com.v2p.swp391.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@RequiredArgsConstructor
public class GhnRestTemplate extends RestTemplate{
    public GhnRestTemplate(String token,Integer shopId ){
        super();
        // Thiết lập URI template handler cho RestTemplate
        this.setUriTemplateHandler(new DefaultUriBuilderFactory("https://dev-online-gateway.ghn.vn"));

        // Tạo HttpHeaders cho RestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("ShopId", shopId.toString());

        // Tạo một ClientHttpRequestInterceptor để thêm header vào mỗi request
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().addAll(headers);
            return execution.execute(request, body);
        };

        // Thêm interceptor vào RestTemplate
        this.getInterceptors().add(interceptor);
    }

}
