package com.base.seed.common.env;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * IP address utility.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpUtils {

  public static final String IP_REGEX =
      "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)){3})";

  private static final String PREFERRED_NETWORK_INTERFACE = "seed.preferred.network.interface";

  private static volatile String cachedIpAddress;

  private static volatile String cachedHostName;

  /**
   * Get IP address for localhost.
   *
   * @return IP address for localhost
   */
  public static String getIp() {
    if (null != cachedIpAddress) {
      return cachedIpAddress;
    }
    NetworkInterface networkInterface = findNetworkInterface();
    if (null != networkInterface) {
      Enumeration<InetAddress> ipAddresses = networkInterface.getInetAddresses();
      while (ipAddresses.hasMoreElements()) {
        InetAddress ipAddress = ipAddresses.nextElement();
        if (isValidAddress(ipAddress)) {
          cachedIpAddress = ipAddress.getHostAddress();
          return cachedIpAddress;
        }
      }
    }
    throw new HostException("ip is null");
  }

  private static NetworkInterface findNetworkInterface() {
    Enumeration<NetworkInterface> interfaces;
    try {
      interfaces = NetworkInterface.getNetworkInterfaces();
    } catch (final SocketException ex) {
      throw new HostException(ex);
    }
    List<NetworkInterface> validNetworkInterfaces = new LinkedList<>();
    while (interfaces.hasMoreElements()) {
      NetworkInterface networkInterface = interfaces.nextElement();
      if (ignoreNetworkInterface(networkInterface)) {
        continue;
      }
      validNetworkInterfaces.add(networkInterface);
    }
    NetworkInterface result = null;
    for (NetworkInterface each : validNetworkInterfaces) {
      if (isPreferredNetworkInterface(each)) {
        result = each;
        break;
      }
    }
    if (null == result) {
      result = getFirstNetworkInterface(validNetworkInterfaces);
    }
    return result;
  }

  private static NetworkInterface getFirstNetworkInterface(final List<NetworkInterface> validNetworkInterfaces) {
    NetworkInterface result = null;
    for (NetworkInterface each : validNetworkInterfaces) {
      Enumeration<InetAddress> addresses = each.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress inetAddress = addresses.nextElement();
        if (isValidAddress(inetAddress)) {
          result = each;
          break;
        }
      }
    }
    if (null == result && !validNetworkInterfaces.isEmpty()) {
      result = validNetworkInterfaces.get(0);
    }
    return result;
  }

  private static boolean isPreferredNetworkInterface(final NetworkInterface networkInterface) {
    String preferredNetworkInterface = System.getProperty(PREFERRED_NETWORK_INTERFACE);
    return Objects.equals(networkInterface.getDisplayName(), preferredNetworkInterface);
  }

  private static boolean ignoreNetworkInterface(final NetworkInterface networkInterface) {
    try {
      return null == networkInterface
          || networkInterface.isLoopback()
          || networkInterface.isVirtual()
          || !networkInterface.isUp();
    } catch (final SocketException ex) {
      return true;
    }
  }

  private static boolean isValidAddress(final InetAddress inetAddress) {
    try {
      return !inetAddress.isLoopbackAddress() && !inetAddress.isAnyLocalAddress()
          && !isIp6Address(inetAddress) && inetAddress.isReachable(100);
    } catch (final IOException ex) {
      return false;
    }
  }

  private static boolean isIp6Address(final InetAddress ipAddress) {
    return ipAddress instanceof Inet6Address;
  }

  /**
   * Get host name for localhost.
   *
   * @return host name for localhost
   */
  public static String getHostName() {
    if (null != cachedHostName) {
      return cachedHostName;
    }
    try {
      cachedHostName = InetAddress.getLocalHost().getHostName();
    } catch (final UnknownHostException ex) {
      cachedHostName = "unknown";
    }
    return cachedHostName;
  }
}
