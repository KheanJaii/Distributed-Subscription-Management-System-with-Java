import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aboneler implements Serializable {
    private static final long serialVersionUID = 1L;

    private long lastUpdatedEpochMiliSeconds;
    private List<Boolean> abonelerListesi;
    private List<Boolean> girisYapanlarListesi;

    public Aboneler() {
        lastUpdatedEpochMiliSeconds = 0;
        abonelerListesi = new ArrayList<>();
        girisYapanlarListesi = new ArrayList<>();
    }

    public long getEpochMiliSeconds() {
        return lastUpdatedEpochMiliSeconds;
    }

    public void setEpochMiliSeconds(long lastUpdatedEpochMiliSeconds) {
        this.lastUpdatedEpochMiliSeconds = lastUpdatedEpochMiliSeconds;
    }

    public List<Boolean> getAboneler() {
        return abonelerListesi;
    }

    public void setAboneler(List<Boolean> aboneler) {
        abonelerListesi = aboneler;
    }

    public List<Boolean> getGirisYapanlarListesi() {
        return girisYapanlarListesi;
    }

    public void setGirisYapanlarListesi(List<Boolean> girisYapanlarListesi) {
        this.girisYapanlarListesi = girisYapanlarListesi;
    }

    public void aboneEkle(int index) {
        if (index >= 0 && index < abonelerListesi.size()) {
            abonelerListesi.set(index, true);
            lastUpdatedEpochMiliSeconds = System.currentTimeMillis();
        }
    }

    public void abonelikIptal(int index) {
        if (index >= 0 && index < abonelerListesi.size()) {
            abonelerListesi.set(index, false);
            lastUpdatedEpochMiliSeconds = System.currentTimeMillis();
        }
    }

    public void girisYap(int index) {
        if (index >= 0 && index < girisYapanlarListesi.size()) {
            girisYapanlarListesi.set(index, true);
            lastUpdatedEpochMiliSeconds = System.currentTimeMillis();
        }
    }

    public void cikisYap(int index) {
        if (index >= 0 && index < girisYapanlarListesi.size()) {
            girisYapanlarListesi.set(index, false);
            lastUpdatedEpochMiliSeconds = System.currentTimeMillis();
        }
    }
}
