package pe.factos.rendering.domain.model.commands;

public record GenerateQrCommand(String content, int width, int height) {
    public GenerateQrCommand(String content) {
        this(content, 200, 200);
    }
}
