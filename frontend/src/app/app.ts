import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ContactSection } from './features/home/components/contact-section/contact-section';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ContactSection],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
}
