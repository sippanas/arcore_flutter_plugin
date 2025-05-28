import 'dart:ui';

extension ColorExtension on Color {
  int toInt() {
    int alpha = a.toInt();
    int red = r.toInt();
    int green = g.toInt();
    int blue = b.toInt();

    return (alpha << 24) | (red << 16) | (green << 8) | blue;
  }
}