import java.io.*;
import java.util.Base64;

public class ASUPProtokol {
    private static final String RESPONSE_SUCCESS = "55 TAMM";
    private static final String RESPONSE_ERROR = "50 HATA";

    public String abonolRequest(int index) {
        return "ABONOL " + index;
    }

    public String abonIptalRequest(int index) {
        return "ABONPTAL " + index;
    }

    public String girisRequest(int index) {
        return "GIRIS ISTMC " + index;
    }

    public String cikisRequest(int index) {
        return "CIKIS ISTMC " + index;
    }

    public String serilestirilmisNesneRequest(Aboneler aboneler) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(aboneler);
            oos.flush();
            byte[] bytes = bos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String responseMessage(boolean success, String message) {
        return success ? RESPONSE_SUCCESS : RESPONSE_ERROR + " " + message;
    }

    public String errorMessage(String message) {
        return RESPONSE_ERROR + " " + message;
    }

    public Aboneler deserializeNesne(String serialized) {
        try {
            byte[] bytes = Base64.getDecoder().decode(serialized);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (Aboneler) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String abonelerUpdateMessage(Aboneler aboneler) {
        return "SERILESTIRILMIS_NESNE " + serilestirilmisNesneRequest(aboneler);
    }
}
