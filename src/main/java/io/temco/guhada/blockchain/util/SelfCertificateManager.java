package io.temco.guhada.blockchain.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class SelfCertificateManager {

    public SelfCertificateManager(){}
    public File createCaCert(String password)  {
        try {
            File keystore = getKeyStore(password);
            File rootCert = exportCert(keystore, password);
            genKeyPair(keystore, password, getServerAlias(), getServerDName(), getSan());
            File serverCsr = new File("temco.csr");
            certReq(keystore, password, getServerAlias(), serverCsr);

            File serverCert = new File("temco-server.cer");
            genCert(keystore, password, getRootAlias(), serverCsr, serverCert, getSan());

            importCert(keystore, password, getServerAlias(), serverCert);

            FileUtils.deleteQuietly(serverCsr);
            FileUtils.deleteQuietly(serverCert);
            FileUtils.deleteQuietly(rootCert);
            return keystore;
        } catch (Exception e) {
            e.printStackTrace();
            return new File("temco-keystore.jks");
        }
    }

    private File exportCert(File keystore, String password) throws Exception {
        File certFile = new File("temco.cer");
        export(keystore, password, getRootAlias(), certFile);
        return certFile;
    }

    private File getKeyStore(String password) throws Exception {
        File keyStore = new File("temco-keystore.jks");
        FileUtils.deleteQuietly(keyStore);
        // delete any existing keystore
        genKeyPair(keyStore, password, getRootAlias(), getRootDName(), getBcCa());
        return keyStore;
    }

    private String getRootAlias() { return "root"; }
    private String getServerAlias() { return "temcolab"; }
    private String getBcCa() { return "bc=ca:true";}
    private String getRootDName() { return "CN=TemcoCA"; }
    private String getServerDName() { return "CN=Temco.io"; }
    private String getSan() throws UnknownHostException {
        return String.format("san=dns:%s,ip:%s", getHostName(), getHostIpAddress());
    }

    private String getHostIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getCanonicalHostName();
    }
    private void genKeyPair(File keyStore, String storepass, String alias, String dname, String ext) throws Exception {
        runKeytool("-genkeypair",
                "-keystore", keyStore.getCanonicalPath(),
                "-storetype", "PKCS12",
                "-storepass", storepass,
                "-alias", alias,
                "-keyalg","RSA",
                "-dname", dname,
                "-ext", ext,
                "-validity", String.valueOf(365));
    }

    private void export(File keyStore, String storePass, String alias, File file) throws Exception {
        runKeytool("-export",
                "-keystore", keyStore.getCanonicalPath(),
                "-storetype", "PKCS12",
                "-storepass", storePass,
                "-alias", alias,
                "-file", file.getCanonicalPath());
    }

    private void certReq(File keyStore, String storePass, String alias, File file) throws Exception {
        runKeytool("-certreq",
                "-keystore", keyStore.getCanonicalPath(),
                "-storetype", "PKCS12",
                "-storepass", storePass,
                "-alias", alias,
                "-file", file.getCanonicalPath());
    }

    private void genCert(File keyStore, String storePass, String alias, File infile, File outfile, String ext)
            throws Exception {
        runKeytool("-gencert",
                "-keystore", keyStore.getCanonicalPath(),
                "-storetype", "PKCS12",
                "-storepass", storePass,
                "-alias", alias,
                "-ext", ext,
                "-validity", String.valueOf(365),
                "-infile", infile.getCanonicalPath(),
                "-outfile", outfile.getCanonicalPath());

    }

    private void importCert(File keyStore, String storePass, String alias, File file) throws Exception {
        runKeytool("-importcert",
                "-keystore", keyStore.getCanonicalPath(),
                "-storetype", "PKCS12",
                "-storepass", storePass,
                "-alias", alias,
                "-file", file.getCanonicalPath());
    }

    private void runKeytool(String... args) throws Exception {
        sun.security.tools.keytool.Main.main(args);
    }

}
