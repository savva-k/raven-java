package com.getsentry.raven.servlet;

import com.getsentry.raven.Raven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Request listener in charge of capturing {@link HttpServletRequest} to allow
 * {@link com.getsentry.raven.event.helper.HttpEventBuilderHelper} to provide details on the current HTTP session
 * in the event sent to Sentry.
 */
public class RavenServletRequestListener implements ServletRequestListener {
    private static final Logger logger = LoggerFactory.getLogger(RavenServletRequestListener.class);

    private static final ThreadLocal<HttpServletRequest> THREAD_REQUEST = new ThreadLocal<HttpServletRequest>();

    public static HttpServletRequest getServletRequest() {
        return THREAD_REQUEST.get();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        THREAD_REQUEST.remove();

        try {
            Raven raven = Raven.getStoredInstance();
            if (raven != null) {
                raven.getContext().clear();
            }
        } catch (Exception e) {
            logger.error("Error clearing Context state.", e);
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        ServletRequest servletRequest = servletRequestEvent.getServletRequest();
        if (servletRequest instanceof HttpServletRequest) {
            THREAD_REQUEST.set((HttpServletRequest) servletRequest);
        }
    }
}
