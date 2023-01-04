package com.pas.util;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

public class MyHandshakeCompletedListener implements HandshakeCompletedListener 
{	
	@Override
	public void handshakeCompleted(HandshakeCompletedEvent event) 
	{
	   SSLSession session = event.getSession();
	   String protocol = session.getProtocol();
	   String cipherSuite = session.getCipherSuite();
	   String peerName = null;
	
       try 
	   {
	       peerName = session.getPeerPrincipal().getName();
	   }
	   catch (SSLPeerUnverifiedException e) 
	   {
	   }
	}   
}

	