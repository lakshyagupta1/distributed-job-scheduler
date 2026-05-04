import React, { useState } from "react";

function FileUpload() {

  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);

  // file select
  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (!selectedFile) return;

    setFile(selectedFile);
  };

  // upload
  const handleUpload = async () => {

    if (!file) {
      alert("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    setUploading(true);
    setProgress(10);

    let prog = 10;

    const interval = setInterval(() => {
      prog += 5;
      if (prog < 90) setProgress(prog);
    }, 200);

    await fetch("http://localhost:8080/jobs/upload-file", {
      method: "POST",
      body: formData,
    });

    clearInterval(interval);
    setProgress(100);
    setUploading(false);
  };

  return (
    <div className="upload-card">

      {/* 🔹 Title */}
      <h2 >
        📁 File Job Upload
      </h2>

      {/* 🔹 Row layout */}
      

        {/* File input */}
        
          <input
            type="file"
            onChange={handleFileChange}
            style={{ color: "#cbd5f5" }}
          />

          {/* file name */}
          {file && (
            <p style={{
              fontSize: "12px",
              color: "#94a3b8",
              marginTop: "5px"
            }}>
              Selected: {file.name}
            </p>
          )}
        

        {/* Upload button */}
        <button
          onClick={handleUpload}
          style={{
            padding: "8px 16px",
            background: "#3b82f6",
            color: "white",
            border: "none",
            borderRadius: "6px",
            cursor: "pointer"
          }}
        >
          Upload File Job
        </button>

      

      {/* 🔥 Progress bar */}
      {uploading && (
        <div style={{ marginTop: "15px" }}>
          
          <div style={{
            height: "10px",
            background: "#334155",
            borderRadius: "10px",
            overflow: "hidden"
          }}>
            <div style={{
              width: `${progress}%`,
              background: "#8b5cf6",
              height: "100%",
              transition: "0.3s"
            }} />
          </div>

          <p style={{ marginTop: "5px", color: "#94a3b8" }}>
            Uploading... {progress}%
          </p>
        </div>
      )}

    </div>
  );
}

export default FileUpload;