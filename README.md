# Restoran Menu — Android ilova + Admin Panel

Bu repo ikkita qismdan iborat:

1. **`app/`** — Android ilova (Kotlin + Jetpack Compose + Material 3 + Firebase Firestore)
2. **`admin-panel/`** — Brauzerda ochiladigan veb-admin panel (Firestore'dagi kategoriya va taomlarni boshqarish uchun)

> ⚠️ **Muhim:** Ushbu kod bu yerda avtomatik **.apk fayl sifatida kompilyatsiya qilinmagan**, chunki bu muhitda Android SDK/Gradle va internet mavjud emas. Quyidagi qadamlarni bajarib, o'zingizning kompyuteringizda Android Studio orqali 5-10 daqiqada APK yig'ib olasiz.

---

## 1. Loyiha arxitekturasi (nega bu tuzilma tanlangan)

```
app/src/main/java/com/example/restaurantmenu/
├── data/
│   ├── model/          -> Category.kt, MenuItem.kt (Firestore data class'lari)
│   └── repository/     -> MenuRepository.kt (Firestore bilan yagona aloqa nuqtasi)
├── viewmodel/           -> MenuViewModel.kt (UI state boshqaruvi)
├── navigation/          -> Routes.kt, RestaurantNavGraph.kt (barcha sahifalar shu yerda ro'yxatga olinadi)
└── ui/
    ├── splash/          -> SplashScreen.kt
    ├── home/            -> HomeScreen.kt
    ├── menu/            -> MenuScreen.kt, CategoryDetailScreen.kt
    ├── about/            -> AboutScreen.kt
    ├── components/       -> FoodCard.kt (qayta ishlatiladigan kartochka)
    └── theme/            -> Material 3 rang/shrift sozlamalari
```

**Nega shunday qilindi:** Firestore bilan ishlash faqat `MenuRepository.kt` ichida. Kelajakda buyurtma/savat/to'lov qo'shmoqchi bo'lsangiz:
- `data/model/` ga `Order.kt`, `CartItem.kt` qo'shasiz
- `data/repository/` ga `OrderRepository.kt` qo'shasiz
- `navigation/Routes.kt` ga yangi route qo'shasiz
- `ui/` ostida yangi papka (masalan `ui/cart/`) ochasiz

Mavjud kodni buzmasdan, faqat qo'shimcha fayllar bilan kengaytirish mumkin.

---

## 2. Firebase sozlash (majburiy qadam)

1. https://console.firebase.google.com ga kiring, yangi loyiha yarating.
2. **Build → Firestore Database → Create database** ni bosing (test mode bilan boshlashingiz mumkin, keyin xavfsizlik qoidalarini kuchaytirasiz).
3. **Project settings → Your apps → Add app → Android** tanlang:
   - Package name: `com.example.restaurantmenu`
4. Yuklab olingan `google-services.json` faylini `app/` papkasiga qo'ying (bu repoda `app/google-services.json.EXAMPLE` — shu nomga o'zgartiring va real fayl bilan almashtiring).
5. Xuddi shu Firebase loyihasida **Add app → Web** ni ham qo'shing (admin panel uchun) va SDK config'ni `admin-panel/index.html` ichidagi `firebaseConfig` ga joylashtiring.

### Firestore struktura

```
categories (collection)
  pizza (doc)      { name: "Pizza",      order: 0 }
  lavash (doc)     { name: "Lavash",     order: 1 }
  burger (doc)     { name: "Burger",     order: 2 }
  donar (doc)      { name: "Donar",      order: 3 }
  ichimlik (doc)   { name: "Ichimliklar",order: 4 }

menuItems (collection)
  {auto-id} (doc)  {
    categoryId: "pizza",
    name: "Margarita",
    price: 45000,
    imageUrl: "https://...",
    available: true
  }
```

Namuna ma'lumotlar `firestore-sample-data.json` faylida mavjud. Ularni **admin panel** orqali (eng qulay usul) yoki Firebase Console'dan qo'lda kiritishingiz mumkin.

---

## 3. Admin panelni ishga tushirish

`admin-panel/index.html` — hech qanday build kerak emas, oddiy statik fayl:

1. `firebaseConfig` qismini o'z Firebase Web App ma'lumotlaringiz bilan to'ldiring.
2. Faylni istalgan brauzerda oching (yoki Firebase Hosting / GitHub Pages / Netlify'ga joylashtiring).
3. "Kategoriyalar" bo'limidan avval kategoriya qo'shing, keyin "Taomlar" bo'limidan taom qo'shing.
4. Barcha o'zgarishlar **real vaqtda** Firestore orqali ilovaga ham tarqaladi — ilovani qayta build qilish shart emas.

Xavfsizlik eslatmasi: Bu oddiy demo panel bo'lib, hozircha login/parol yo'q. Productionda buni parol bilan himoyalash yoki faqat ishonchli tarmoqqa joylashtirish tavsiya etiladi (masalan Firebase Authentication qo'shib, faqat admin email kira oladigan qilish).

---

## 4. Android Studio'da ochish va APK yig'ish (Kompyuter orqali)

1. [Android Studio](https://developer.android.com/studio) o'rnating (eng so'ngi versiya).
2. `restaurant-menu-app` papkasini **Open** qiling.
3. Gradle sync tugashini kuting (internet talab qiladi — bog'liqliklarni yuklab oladi).
4. `app/google-services.json` haqiqiy fayl ekanligiga ishonch hosil qiling.
5. Ilova ikonkasini almashtirmoqchi bo'lsangiz: **Right-click `res` → New → Image Asset** orqali haqiqiy logotipingizni yuklang (hozir oddiy placeholder ikonka bor).
6. Test qilish uchun: qurilma yoki emulyator tanlab **Run ▶** bosing.
7. APK olish uchun: **Build → Build Bundle(s)/APK(s) → Build APK(s)**.
   - Natija: `app/build/outputs/apk/debug/app-debug.apk`
8. Play Store'ga chiqarish uchun **imzolangan** (signed) APK/AAB kerak: **Build → Generate Signed Bundle/APK**.

---

## 4-B. Faqat TELEFON orqali APK olish (Android Studio'siz)

Agar kompyuteringiz bo'lmasa, kod **GitHub Actions** orqali bulutda avtomatik build bo'ladi va tayyor APK faylni to'g'ridan-to'g'ri telefoningizga yuklab olasiz. Loyihada tayyor `.github/workflows/build-apk.yml` fayli bor.

**Qadamlar (barchasi telefon brauzeri yoki GitHub ilovasi orqali):**

1. **GitHub'da account oching** (agar yo'q bo'lsa): github.com
2. **Yangi repository yarating** (masalan `restaurant-menu-app`), Public yoki Private — farqi yo'q.
3. ZIP arxivni GitHub'ga yuklang (`google-services.json` fayli allaqachon `app/` papkada tayyor turibdi — qo'shimcha sozlash shart emas):
   - GitHub saytida repo ichida **"Add file" → "Upload files"** tugmasini bosing
   - ZIP'ni ochib (telefonda "Files" ilovasi orqali extract qiling), barcha fayl va papkalarni tanlab yuklang
4. Repo → **Actions** bo'limiga o'ting → **"Build Debug APK"** workflow'ni tanlang → **"Run workflow"** tugmasini bosing.
5. Bir necha daqiqadan so'ng (build tugagach) workflow natijasi ichida **Artifacts → app-debug-apk** paydo bo'ladi — shu yerdan APK faylni telefoningizga yuklab olasiz.
6. Yuklab olingan `.apk` faylni oching — telefon "noma'lum manbadan o'rnatish"ga ruxsat so'raydi, ruxsat bering va ilova o'rnatiladi.

> 💡 Eslatma: Bu **debug APK** — sinov uchun yetarli. Play Store'ga chiqarish uchun imzolangan (signed) versiya kerak bo'ladi, buni faqat Android Studio orqali (yoki workflow'ni kengaytirib) qilish tavsiya etiladi.

---

## 5. Keyingi bosqichda qo'shish oson bo'lgan funksiyalar

- 🛒 Savatcha va buyurtma berish (`ui/cart/`, `data/model/Order.kt`)
- 💳 To'lov integratsiyasi (Payme/Click)
- 🔐 Admin panelga Firebase Authentication bilan login qo'shish
- 🌐 Ko'p tillilik (uz/ru/en) — `strings.xml` orqali
- ⭐ Taomlar uchun reyting/sharh tizimi
- 🔍 Qidiruv paneli (menu ichida taom qidirish)

Arxitektura shu narsalarni kiritishga tayyor holda qurilgan — repository/viewmodel/navigation qatlamlari alohida bo'lgani uchun UI'ni buzmasdan kengaytirish mumkin.
