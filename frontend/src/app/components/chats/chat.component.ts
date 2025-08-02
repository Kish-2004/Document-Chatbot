import {
  Component,
  OnInit,
  ElementRef,
  ViewChild,
  AfterViewChecked,
  ChangeDetectorRef
} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageComponent } from '../message/message.component';

declare var window: any;

interface Message {
  text: string;
  isUser: boolean;
  isTyping?: boolean;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, MessageComponent],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('chatMessagesContainer') private chatMessagesContainer!: ElementRef;

  currentMessage: string = '';
  messages: Message[] = [];
  isLoading: boolean = false;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.messages.push({
      text: "Hello! How can I help you today regarding your documents?",
      isUser: false
    });
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  sendMessage(): void {
    const trimmed = this.currentMessage.trim();
    if (!trimmed) return;

    this.messages.push({ text: trimmed, isUser: true });
    this.currentMessage = '';
    this.isLoading = true;
    this.messages.push({ text: '', isUser: false, isTyping: true });
    this.cdr.detectChanges();

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post('http://localhost:8080/api/chat', { question: trimmed }, {
      headers,
      responseType: 'text',
      withCredentials: true
    }).subscribe({
      next: (response: string) => {
        this.removeTypingMessage();
        this.messages.push({ text: response, isUser: false });
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error fetching chat response:', error);
        this.removeTypingMessage();
        this.messages.push({
          text: "Oops! Something went wrong. Please try again.",
          isUser: false
        });
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  removeTypingMessage(): void {
    this.messages = this.messages.filter(msg => !msg.isTyping);
  }

  scrollToBottom(): void {
    try {
      this.chatMessagesContainer.nativeElement.scrollTop =
        this.chatMessagesContainer.nativeElement.scrollHeight;
    } catch (err) {}
  }

  startVoiceInput(): void {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) {
      alert("Your browser doesn't support voice input.");
      return;
    }

    const recognition = new SpeechRecognition();
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    recognition.onstart = () => {
      console.log("Voice recognition started...");
    };

    recognition.onerror = (event: any) => {
      console.error("Speech recognition error:", event.error);
      alert("Error: " + event.error);
    };

    recognition.onresult = (event: any) => {
      const transcript = event.results[0][0].transcript;
      console.log("Recognized speech:", transcript);
      this.currentMessage = transcript;
      this.cdr.detectChanges();
    };

    recognition.start();
  }
}
