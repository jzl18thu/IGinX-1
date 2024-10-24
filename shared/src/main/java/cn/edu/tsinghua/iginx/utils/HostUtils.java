/*
 * IGinX - the polystore system with high performance
 * Copyright (C) Tsinghua University
 * TSIGinX@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package cn.edu.tsinghua.iginx.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class HostUtils {

  private static final String IPV4_REGEX = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";

  private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

  private static final Set<String> LOCAL_HOST_ADDRESS_SET = new HashSet<>();

  static {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress inetAddress = inetAddresses.nextElement();
          if (inetAddress instanceof Inet4Address) {
            LOCAL_HOST_ADDRESS_SET.add(inetAddress.getHostAddress());
          }
        }
      }
    } catch (SocketException e) {
      System.out.printf("get local host address error: %s%n", e.getMessage());
    }
  }

  public static boolean isLocalHost(String host) {
    for (String localHostAddress : LOCAL_HOST_ADDRESS_SET) {
      if (host.equals(localHostAddress)) {
        return true;
      }
    }
    return false;
  }

  //  public static boolean isLocalHost(String host) {
  //    try {
  //      InetAddress address = InetAddress.getByName(host);
  //      if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
  //        return true;
  //      }
  //      return NetworkInterface.getByInetAddress(address) != null;
  //    } catch (UnknownHostException | SocketException e) {
  //      return false;
  //    }
  //  }

  public static boolean isValidHost(String host) {
    if (host == null || host.trim().isEmpty()) {
      return false;
    }
    if (host.equals("host.docker.internal")) {
      // using docker
      return true;
    }
    if (!IPV4_PATTERN.matcher(host).matches()) {
      return false;
    }
    String[] parts = host.split("\\.");
    try {
      for (String part : parts) {
        if (Integer.parseInt(part) > 255 || (part.length() > 1 && part.startsWith("0"))) {
          return false;
        }
      }
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  //  // host name --> host address
  //  public static String convertHostNameToHostAddress(String hostName) {
  //    String hostAddress = hostName;
  //    try {
  //      InetAddress address = InetAddress.getByName(hostName);
  //      hostAddress = address.getHostAddress();
  //    } catch (UnknownHostException e) {
  //      return hostAddress;
  //    }
  //    return hostAddress;
  //  }
}
