# VkVideoTests
Тесты для VK Видео

Сделал топорное решение "в лоб". Задержки вида sleep(10000) для ожидания полной загрузки.

Окружение:
Oracle OpenJDK 22.0.1;
Maven 3.9.6;
Appium v.3.2.2 с UiAutomator2 v.7.0.0;
Android Studio 2025.3.2 Panda 2, Virtual Device API 30 "R";Android 11.0;Google Play Intel x86 Atom System Image.

Перед запуском для "appium:app" указать полный путь до ".../src/test/resources/VK-Video-v1.136.apk"
(инструкция по работе git с большими файлами https://git-lfs.com/).
Запуск: mvn clean install