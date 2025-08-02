// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { UploadComponent } from './components/upload/upload.component'; // Ensure this path is correct
import { ChatComponent } from './components/chats/chat.component';     // Corrected path: 'chat' instead of 'chats'

export const routes: Routes = [
  { path: 'upload', component: UploadComponent },
  { path: 'chat', component: ChatComponent },
  { path: '', redirectTo: '/upload', pathMatch: 'full' }, // Default route to upload page
  { path: '**', redirectTo: '/upload' } // Wildcard route for any other invalid path
];
