package net.lab1024.smartadmin.service.config;

import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.text.WordRenderer;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.Configurable;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * [ 验证码配置 ]
 *
 * @author zhuoda
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.border.color", "34,114,200");
        properties.setProperty("kaptcha.image.width", "125");
        properties.setProperty("kaptcha.image.height", "45");
        properties.setProperty("kaptcha.textproducer.char.string", "123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana");
        properties.setProperty("kaptcha.textproducer.font.size", "38");

        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");

        properties.setProperty("kaptcha.word.impl", KaptchaWordRenderer.class.getName());
        properties.setProperty("kaptcha.noise.impl", KaptchaNoise.class.getName());

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }


    public static class KaptchaColor {

        public static Color getColor() {

            List<Color> colors = Lists.newArrayList();
            colors.add(new Color(0, 135, 255));
            colors.add(new Color(51, 153, 51));
            colors.add(new Color(255, 102, 102));
            colors.add(new Color(255, 153, 0));
            colors.add(new Color(153, 102, 0));
            colors.add(new Color(153, 102, 153));
            colors.add(new Color(51, 153, 153));
            colors.add(new Color(102, 102, 255));
            colors.add(new Color(0, 102, 204));
            colors.add(new Color(204, 51, 51));
            colors.add(new Color(128, 153, 65));
            Random random = new Random();
            int colorIndex = random.nextInt(10);
            return colors.get(colorIndex);
        }
    }

    public class KaptchaNoise extends Configurable implements NoiseProducer {

        public KaptchaNoise() {
        }

        @Override
        public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {

            int width = image.getWidth();
            int height = image.getHeight();
            Graphics2D graph = (Graphics2D) image.getGraphics();
            graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            graph.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            Random random = new Random();
            int noiseLineNum = random.nextInt(3);
            if (noiseLineNum == 0) {
                noiseLineNum = 1;
            }
            for (int i = 0; i < noiseLineNum; i++) {
                graph.setColor(KaptchaColor.getColor());
                graph.drawLine(random.nextInt(width), random.nextInt(height), 10 + random.nextInt(20), 10 + random.nextInt(20));
            }

            graph.dispose();
        }
    }

    public class KaptchaWordRenderer extends Configurable implements WordRenderer {

        public KaptchaWordRenderer() {
        }

        @Override
        public BufferedImage renderWord(String word, int width, int height) {
            int fontSize = this.getConfig().getTextProducerFontSize();
            Font[] fonts = this.getConfig().getTextProducerFonts(fontSize);
            int charSpace = this.getConfig().getTextProducerCharSpace();
            BufferedImage image = new BufferedImage(width, height, 2);

            Graphics2D g2D = image.createGraphics();
            g2D.setColor(Color.WHITE);
            RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2D.setRenderingHints(hints);
            g2D.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

            FontRenderContext frc = g2D.getFontRenderContext();
            Random random = new Random();
            int startPosY = (height - fontSize) / 5 + fontSize;
            char[] wordChars = word.toCharArray();
            Font[] chosenFonts = new Font[wordChars.length];
            int[] charWidths = new int[wordChars.length];
            int widthNeeded = 0;

            int startPosX;
            for (startPosX = 0; startPosX < wordChars.length; ++startPosX) {
                chosenFonts[startPosX] = fonts[random.nextInt(fonts.length)];
                char[] charToDraw = new char[]{wordChars[startPosX]};
                GlyphVector gv = chosenFonts[startPosX].createGlyphVector(frc, charToDraw);
                charWidths[startPosX] = (int) gv.getVisualBounds().getWidth();
                if (startPosX > 0) {
                    widthNeeded += 2;
                }

                widthNeeded += charWidths[startPosX];
            }

            startPosX = (width - widthNeeded) / 2;

            for (int i = 0; i < wordChars.length; ++i) {
                g2D.setColor(KaptchaColor.getColor());
                g2D.setFont(chosenFonts[i].deriveFont(Font.PLAIN));
                char[] charToDraw = new char[]{wordChars[i]};
                g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
                startPosX = startPosX + charWidths[i] + charSpace;
            }

            return image;
        }

    }
}
