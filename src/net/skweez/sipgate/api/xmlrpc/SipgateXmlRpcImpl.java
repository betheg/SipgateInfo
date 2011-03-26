/*-----------------------------------------------------------------------+
 | sipgate Kontostand
 |                                                                       |
   $Id: codetemplates.xml 9 2009-03-06 10:09:52Z mks $            
 |                                                                       |
 | Copyright (c)  2004-2011 Technische Universitaet Muenchen             |
 |                                                                       |
 | Technische Universitaet Muenchen               #########  ##########  |
 | Institut fuer Informatik - Lehrstuhl IV           ##  ##  ##  ##  ##  |
 | Prof. Dr. Manfred Broy                            ##  ##  ##  ##  ##  |
 | Boltzmannstr. 3                                   ##  ##  ##  ##  ##  |
 | 85748 Garching bei Muenchen                       ##  ##  ##  ##  ##  |
 | Germany                                           ##  ######  ##  ##  |
 +-----------------------------------------------------------------------*/
package net.skweez.sipgate.api.xmlrpc;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.Map;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.SipgateException;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class SipgateXmlRpcImpl implements ISipgateAPI {

	private static final URI API_URI;

	static {
		API_URI = URI.create("https://samurai.sipgate.net/RPC2");
	}

	private final XMLRPCClient client;

	public SipgateXmlRpcImpl() {
		PasswordAuthentication authentication = Authenticator
				.requestPasswordAuthentication(null, 80, "http", null, null);

		String username = authentication.getUserName();
		String password = String.valueOf(authentication.getPassword());

		client = new XMLRPCClient(API_URI, username, password);
	}

	/** {@inheritDoc} */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Price getBalance() {
		Map<String, Map> result = (Map<String, Map>) executeMethod("samurai.BalanceGet");
		Map currentBalance = result.get("CurrentBalance");

		return new Price((Double) currentBalance.get("TotalIncludingVat"),
				(String) currentBalance.get("Currency"));
	}

	public Map<String, ? extends Object> executeMethod(String method,
			String... params) {
		try {
			return (Map<String, Object>) client.callEx(method, params);
		} catch (final XMLRPCException exception) {
			throw new SipgateException(exception);
		}
	}
}