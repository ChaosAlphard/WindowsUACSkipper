module io.github.chaosalphard.windowsuacskipper {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core.jvm;

    requires org.kordamp.bootstrapfx.core;

    opens io.github.chaosalphard.windowsuacskipper.controller to javafx.fxml;
    exports io.github.chaosalphard.windowsuacskipper;
}
