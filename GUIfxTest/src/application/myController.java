package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class myController {
	int num;
	@FXML
	public Label idLabel;

	public myController() {
		super();
		this.num = 0;
	}

	public void increase(ActionEvent event) {
		this.idLabel.setText(this.addOne());

	}

	private String addOne() {
		this.num += 1;
		return Integer.toString(this.num);
	}

	// decrease
	public void decrease(ActionEvent event) {
		this.idLabel.setText(this.removeOne());

	}

	private String removeOne() {
		this.num -= 1;
		return Integer.toString(this.num);
	}

}
