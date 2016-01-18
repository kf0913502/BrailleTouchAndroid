package com.brailletouch.kariem.brailletouchtest;

/**
 * Created by Kariem on 1/17/2016.
 */

import android.os.AsyncTask;
import android.util.Log;


import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Kariem on 7/13/2015.
 */
public class AsyncHTTPTask<P, R> extends AsyncTask<P, Void, R> {

    private Callback<R> F;
    private String resource;
    private String opType;
    private Class returnClass;
    public void setHttpParams(String resource, String opType, Callback<R> F, Class returnClass)
    {
        this.F = F;
        this.resource = resource;
        this.opType = opType;
        this.returnClass = returnClass;
    }

    @Override
    protected R doInBackground(P... params) {
        RestTemplate restTemplate = new RestTemplate();
        R response;
        MappingJackson2HttpMessageConverter mapping =  new MappingJackson2HttpMessageConverter();
        ArrayList<MediaType> types = new ArrayList<MediaType>();
        types.add(new MediaType( "text", "json" ));
        mapping.setSupportedMediaTypes(types);
        switch (opType)
        {
            case "GET":
                restTemplate.getMessageConverters().add(mapping);
                try {
                    response = restTemplate.getForObject(this.resource, (Class<R>) returnClass, params[0]);
                    F.notify(response);
                }
                catch (Exception e)
                {
                    Log.v("BrailleTouchTest", "Failed at retrieving tweets " + e);
                    F.notify(null);
                }
                break;

            case "POST":
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try
                {

                    response = restTemplate.postForObject(this.resource, params[0], (Class<R>)returnClass);
                    F.notify(response);
                }
                catch (Exception e)
                {
                    F.notify(null);
                }
                break;

            case "PUT":
                break;
        }

        return null;
    }
}
