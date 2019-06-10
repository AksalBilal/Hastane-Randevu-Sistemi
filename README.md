# Hastane-Randevu-Sistemi
Kişisel Gelişim Amaçlı Yapılmış bir hastane randevu sistemidir.
Ortak İsterler
Projede ‘kayıtlı kullanıcı (hasta)’, doktor ve ‘admin’ olacak şekilde üç farklı kullanıcı tipi bulunmalıdır. Bu kullanıcıların rolleri kapsamında yapabilecekleri aşağıda sıralanmıştır.
Kayıtlı Kullanıcı (Hasta)
o	Hastalar kayıt olma ekranını kullanarak kendi bilgileri ile sisteme kayıt olabilmelidir.
o	Kayıt olan hastalar giriş ekranını kullanarak sisteme giriş yapabilmelidir. (TCKN ve Şifre ile)
o	Kayıt olma ve giriş işlemlerinde validasyon kontrolleri yapılmalıdır. 
o	Uygulamaya giriş yapan kullanıcı sırasıyla hastane, bölüm (ilgili hastaneye ait bölümler) ve doktor (ilgili bölüme ait doktorlar) seçerek randevu alma işlemini gerçekleştirebilmelidir.
o	Kullanıcı randevu alırken ilgili doktorun randevu için uygun saatlerini görebilmeli ve ona göre bir saat seçimi yapmalıdır. 
o	Saat seçiminin ardından, kullanıcı randevu bilgilerini (hastane, bölüm, doktor, tarih, saat) görüntüledikten sonra randevu işlemini onaylamalıdır. 
o	Randevu işlemi tamamlandıktan sonra randevu alınan doktorun ilgili saati kapatılmalıdır. 
o	Uygulamada, kullanıcının kişisel bilgilerini ve geçmiş randevularını görüntüleyebileceği bir profil ekranı olmalıdır. 
o	Kullanıcı kişisel bilgilerini düzenleyebilmelidir.
o	Kullanıcı henüz tarihi geçmemiş randevularını iptal edebilmelidir. (Randevu düzenleme işlemi olmayacaktır.) 
o	İptal edilen randevu saatleri tekrar randevu alınabilir hale getirilmelidir. 
o	Kullanıcı tedavi olduğu doktoru favorilerine ekleyebilmeli (randevu tarihi geçtikten sonra) ve favori doktorlarını hastane ve bölümü ile birlikte bir sayfada listeleyebilmelidir. 
Doktor
o	Sisteme admin tarafından verilecek bir şifre ve kendi tckn’sini kullanarak giriş yapabilmelidir. 
o	Şifresini değiştirebilmelidir. 
o	Uygulama üzerinden kendisine alınan randevuları, görüntüleme tarihi dâhil 1 aylık süre için görüntüleyebilmelidir. (1 aylık randevu sayısı fazla olacağından sayfalama işlemi yapılmalıdır.)
o	Doktorun görüntüleyeceği randevu bilgileri tarih, saat ve hasta adı, soyadı şeklinde olmalıdır. 
Admin
o	Yeni hastane ekleme / hastane silme / hastane güncelleme işlemlerini yapabilmeli
o	Yeni bölüm ekleme / bölüm silme / bölüm güncelleme işlemlerini yapabilmeli
o	Yeni doktor ekleme / doktor silme / doktor güncelleme işlemlerini yapabilmeli
o	Doktorlara ait randevu saatlerini kapatıp açabilmelidir. 


•	Yukarıdaki kullanıcı rollerinin gerçekleştirileceği tüm ön yüz sayfalarının hazırlanması beklenmektedir. Bu sayfalara ilave olarak kullanıcıyı genel bir hastane randevu sistemine uygun ‘Ana sayfa’ karşılamalıdır.

•	Her bir proje içerisinde en az 5 farklı hastane, 5 farklı bölüm ve her bölümde 5 farklı doktor bulunmalı ve değerlendirme testlerinin yapılabilmesi için yeterli sayıda girdiye sahip olmalıdır.

