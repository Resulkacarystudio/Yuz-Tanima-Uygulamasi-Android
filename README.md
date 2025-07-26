# 👤 Yüz Tanıma Android Uygulaması

Bu proje, Android cihazların ön kamerasını kullanarak **gerçek zamanlı yüz tanıma** yapan bir mobil uygulamadır. Uygulama, Google ML Kit’in yüz algılama API'si ile çalışan basit ve etkili bir yüz tanıma sistemidir.

---

## 📌 Proje Özeti

- 📷 Kamera görüntüsünü PreviewView ile ekrana yansıtır.  
- 🔴 Gerçek zamanlı olarak yüzleri algılar ve algılanan yüzlerin etrafına kırmızı dikdörtgenler çizer.  
- 🤳 Ön kamera kullanılır ve görüntü aynalanarak doğal kullanım sağlanır.  
- 🛠 Android CameraX API ve ML Kit yüz algılama kütüphanesi kullanılmıştır.  
- 🎨 OverlayView sınıfı, algılanan yüzlerin ekran üzerindeki konumunu görsel olarak gösterir.

---

## 🚀 Kullanılan Teknolojiler

- ☕ **Java** — Uygulamanın programlama dili  
- 📸 **Android CameraX API** — Kamera erişimi ve görüntü yönetimi  
- 🤖 **Google ML Kit Face Detection API** — Yüz algılama işlemi için  
- 📐 **ConstraintLayout** — Modern ve esnek arayüz tasarımı  
- 🖼 **AndroidX PreviewView** — Kamera görüntüsünü ekranda göstermek için  
- 🟥 **Özel OverlayView** — Yüzlerin konumlarını kırmızı dikdörtgenler ile işaretleme

---

## 🛠 Uygulama Çalışma Mantığı

1. 🔐 **Kamera İzni Kontrolü:** Uygulama, cihazdan kamera erişim izni ister.  
2. 🎥 **Kamera Başlatma:** Ön kamera seçilir ve PreviewView üzerinde görüntü akışı başlatılır.  
3. 🖼 **Yüz Algılama:** Kamera tarafından alınan görüntü `ImageAnalysis` ile analiz edilir ve ML Kit’e gönderilir.  
4. 📍 **Yüz Koordinatlarının Alınması:** ML Kit, algılanan yüzlerin koordinatlarını döner.  
5. 🟥 **OverlayView ile Çizim:** Algılanan yüzlerin etrafına kırmızı çerçeveler çizilir. Ön kamera olduğu için görüntü yatayda aynalanır.  
6. 🔄 **Gerçek Zamanlı Güncelleme:** Yüz konumları her karede güncellenir.


