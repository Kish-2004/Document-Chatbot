# 🤖 Document Chatbot – Full Stack Spring Boot + Angular + Gemini AI

This full stack project enables users to upload documents (PDF/TXT) and ask questions about their content. The chatbot intelligently extracts text using Apache Tika and responds using Google Gemini AI, all within a sleek Angular frontend.

---

## 🚀 Features

- 📁 Upload `.pdf` and `.txt` files  
- 🧠 Ask natural language questions  
- 🤖 AI answers using Gemini API  
- 🗂️ Text extraction via Apache Tika  
- 🎨 Angular UI with animations and error handling  
- 🔗 Spring Boot backend with REST APIs  
- ✅ CORS enabled for secure cross-origin requests  

---

## 🧾 Project Structure

```
/document--chatbot
├── backend               # Spring Boot backend (Java)
├── frontend              # Angular frontend (TypeScript)
└── uploads               # (Optional) Document upload folder
```

---

## 🔧 Tech Stack

| Layer     | Technology                           |
|-----------|----------------------------------------|
| Frontend  | Angular, RxJS, Bootstrap              |
| Backend   | Spring Boot, Apache Tika, Gemini API |
| Language  | Java, TypeScript                     |
| AI        | Google Gemini API (via API Key)       |
| Parser    | Apache Tika (PDF/TXT extraction)      |

---

## 💻 How to Run the Project

### 🔹 Backend Setup (Spring Boot)

1. ✅ Clone the repository:
```bash
git clone https://github.com/techadminactglobal/Act-Interns-2025.git
cd Act-Interns-2025/KishoreKumar/document--chatbot/backend
```

2. ✅ Configure Gemini API Key in `application.properties`:
```properties
gemini.api.key=YOUR_API_KEY
```

3. ✅ Build and run the backend:
```bash
./mvnw spring-boot:run
```

> Backend runs on: `http://localhost:8080`

---

### 🔹 Frontend Setup (Angular)

1. ✅ Open the `frontend` folder:
```bash
cd ../frontend
```

2. ✅ Install dependencies:
```bash
npm install
```

3. ✅ Run the Angular app:
```bash
ng serve
```

> Frontend runs at: `http://localhost:4200`

---

## 📡 API Endpoints

| Endpoint                 | Method | Description                   |
|--------------------------|--------|-------------------------------|
| `/upload`                | POST   | Upload a document             |
| `/chat?question=...`     | GET    | Ask question and get response |
| `/files/{filename}`      | GET    | (Optional) Retrieve document  |

---

## ✨ Frontend Features

- 📄 Document Upload Interface  
- 💬 Chat Interface with Auto Scroll  
- ⏳ Loader Animation (Thinking Dots)  
- 🔊 Voice Input (Optional via Web Speech API)  
- 🎨 Responsive UI with message bubbles  
- ✅ Toasts for success & errors  

---

## 🧪 Testing & Validation

- ✅ Backend API tested via Postman  
- ✅ Text parsing verified with real PDFs  
- ✅ Gemini answers tested with varied prompts  
- ✅ UI tested on major browsers (Chrome, Edge)  

---

## ✅ Final Checklist

| Feature                  | Status   |
|--------------------------|----------|
| Document Upload          | ✅ Done   |
| Apache Tika Integration  | ✅ Done   |
| Gemini AI Integration    | ✅ Done   |
| Angular Chat UI          | ✅ Working |
| CORS Support             | ✅ Done   |
| API Endpoints            | ✅ Functional |
| Local Deployment         | ✅ Verified |

---

## 🎥 Demo Video

Watch the working demo of the Document Chatbot in action:



https://github.com/user-attachments/assets/f1c7c10d-2145-49a5-bb6e-ec4e227bd0cc


---

## 🧑‍💻 Author

**Kishore Kumar**  
Document Chatbot Project – Act Interns 2025  
Built with Spring Boot, Angular, and Gemini AI

---

## 📄 License

MIT License – Free to use, modify, and distribute.

🔗 **GitHub Repo:** [Document Chatbot](https://github.com/techadminactglobal/Act-Interns-2025/tree/main/KishoreKumar/document--chatbot)
