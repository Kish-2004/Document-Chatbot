import { Component, ChangeDetectorRef } from '@angular/core'; // Import ChangeDetectorRef
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  selectedFile: File | null = null;
  uploadStatus: string | null = null;
  uploadError: string | null = null;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {} // Inject ChangeDetectorRef

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.uploadStatus = null;
    this.uploadError = null;
  }
  onDragOver(event: DragEvent): void {
  event.preventDefault();
}

onFileDrop(event: DragEvent): void {
  event.preventDefault();
  if (event.dataTransfer && event.dataTransfer.files.length > 0) {
    this.selectedFile = event.dataTransfer.files[0];
    this.uploadStatus = null;
    this.uploadError = null;
    this.cdr.detectChanges();
  }
}

  onUpload() {
    if (!this.selectedFile) {
      this.uploadError = "No file selected.";
      return;
    }

    this.uploadStatus = "Uploading...";
    this.uploadError = null;
    this.cdr.detectChanges(); // <--- Add this line to update UI immediately

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);

    this.http.post('http://localhost:8080/api/documents/upload', formData, {
      responseType: 'text'
    })
      .pipe(finalize(() => {
        this.selectedFile = null;
        this.cdr.detectChanges(); // <--- Add this line to ensure UI updates after finalize
      }))
      .subscribe({
        next: (response: string) => {
          this.uploadStatus = "Upload successful! " + response;
          this.cdr.detectChanges(); // <--- Add this line to update UI immediately after success
          console.log('Upload successful', response);
        },
        error: (error: HttpErrorResponse) => {
          this.uploadStatus = null;
          let errorMessage = 'An unknown error occurred.';
          if (error.error instanceof ErrorEvent) {
            errorMessage = `Client Error: ${error.error.message}`;
          } else if (error.error) {
            errorMessage = `Upload failed: ${typeof error.error === 'string' ? error.error : JSON.stringify(error.error)}`;
          } else {
            errorMessage = `Server Error: ${error.status} - ${error.statusText || 'No status text'}`;
          }
          this.uploadError = errorMessage;
          this.cdr.detectChanges(); // <--- Add this line to update UI on error
          console.error('Upload failed', error);
        }
      });
  }
}