import React, { useState } from "react";

function CsvUpload() {

  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [report, setReport] = useState(null);

  // 🔹 File select
  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];

    if (!selectedFile) return;

    if (!selectedFile.name.endsWith(".csv")) {
      alert("Please upload CSV file");
      return;
    }

    setFile(selectedFile);
  };

  // 🔹 Upload on button click
  const handleUpload = async () => {

    if (!file) {
      alert("Please select a file first");
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

    const res = await fetch("http://localhost:8080/jobs/upload", {
      method: "POST",
      body: formData,
    });

    const data = await res.json();

    clearInterval(interval);

    setProgress(100);
    setUploading(false);
    setReport(data);
  };

  return (
    <div className="upload-card">

  
  <div className="upload-left">
    <h2>📤 Bulk Upload Emails (CSV)</h2>
    {!file && (
  <div style={{
    marginTop: "10px",
    padding: "8px",
    borderRadius: "6px",
    color: "#cbd5f5"
  }}>
    Please upload a CSV file to continue
  </div>
)}

    <input type="file" onChange={handleFileChange} />

    <button onClick={handleUpload} style={{
    padding: "8px 16px",
    background: "#3b82f6",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer"
  }}>
    Upload
  </button>
  {uploading && (
  <div style={{ marginTop: "15px", width: "100%" }}>
    
    <div style={{
      height: "10px",
      background: "#334155",
      borderRadius: "10px",
      overflow: "hidden"
    }}>
      <div style={{
        width: `${progress}%`,
        background: "#3b82f6",
        height: "100%",
        transition: "0.3s"
      }} />
    </div>

    <p style={{ marginTop: "5px" }}>{progress}%</p>
  </div>
)}
  </div>

  {/* RIGHT SIDE */}
  <div className="upload-right">
    {report && (
      <>
        <h3>📊 Upload Report</h3>

        <p>Total: <b>{report.total}</b></p>

        <p style={{ color: "#22c55e" }}>
          ✔ Valid: {report.success}
        </p>

        <p style={{ color: "#ef4444" }}>
          ❌ Invalid: {report.failed}
        </p>
      </>
    )}
  </div>

</div>
  );
}

export default CsvUpload;