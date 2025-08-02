// src/app/app.component.ts
import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

// Make sure these paths are correct relative to app.component.ts
import { NavbarComponent } from './components/navbar/navbar.component'; // Correct path to NavbarComponent

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    NavbarComponent // Add NavbarComponent to imports
  ],
  templateUrl: './app.html', // Ensure this path is correct if your HTML file is app.html
  styleUrls: ['./app.css']
})
export class AppComponent {
  protected readonly title = signal('frontend');
}