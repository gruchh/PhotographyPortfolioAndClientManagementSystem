import { Component } from '@angular/core';
import { ContactSection } from "./components/contact-section/contact-section";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ContactSection],
  templateUrl: './home.html',
})
export class Home {

}
