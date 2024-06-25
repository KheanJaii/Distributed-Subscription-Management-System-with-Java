Proje Detayları:

Sunucu Mimarisi:

Server1, Server2, Server3: Her biri belirli görevler üstlenir ve birbirleriyle haberleşir. Sunucular, abone ekleme, abonelik iptali, kullanıcı girişi ve çıkışı gibi işlemleri yönetir.
Mesajlaşma Protokolü:

ASUPProtokol: Sunucular ve istemci arasında haberleşmeyi sağlayan protokoldür. Gelen mesajları işler ve uygun yanıtları üretir.
Abone Yönetimi:

Aboneler Sınıfı: Abonelik ve oturum bilgilerini tutar. Abonelik ekleme, iptal etme, giriş yapma ve çıkış yapma işlemleri bu sınıf üzerinden yönetilir.
İstemci Uygulaması:

Client: Sunuculara mesaj göndererek abonelik ve oturum işlemlerini başlatır. Her sunucuya belirli komutlar gönderir ve sunuculardan gelen yanıtları işler.
Ping İşlemleri:

PingThread: Sunucuların birbirleriyle bağlantı durumlarını kontrol etmek için belirli aralıklarla ping işlemi gerçekleştirir.

Örnek İşlemler:

ABONOL 1: Belirtilen indeksteki aboneliği ekler.
ABONPTAL 2: Belirtilen indeksteki aboneliği iptal eder.
GIRIS ISTMC 33: Belirtilen indeksteki kullanıcıyı oturuma dahil eder.
CIKIS ISTMC 99: Belirtilen indeksteki kullanıcıyı oturumdan çıkarır.

Projenin Amaçları:

Dağıtık sistemlerde abonelik ve kullanıcı oturum yönetimini sağlamak.
Sunucular arasında veri senkronizasyonunu gerçekleştirmek.
Güvenilir ve esnek bir abone yönetim sistemi oluşturmak.

Kullanım Senaryoları:

Gerçek zamanlı uygulamalar için abonelik hizmetleri.
Kullanıcı oturumlarının yönetimi ve takibi.
Dağıtık sistemlerde veri bütünlüğünü koruma ve senkronizasyon sağlama.
