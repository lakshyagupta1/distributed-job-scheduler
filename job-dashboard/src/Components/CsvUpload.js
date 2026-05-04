import React, { useState } from "react";

function CsvUpload() {

  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [report, setReport] = useState(null);

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];

    if (!file) return;

    if (!file.name.endsWith(".csv")) {
      alert("Please upload CSV file");
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

// 🔹 response aate hi
const data = await res.json();

clearInterval(interval);

setProgress(100);
setUploading(false);
setReport(data);
  };

  return (
    <div className="card">
      <h3>Bulk Upload (CSV)</h3>

      <input type="file" accept=".csv" onChange={handleFileUpload} />

      {uploading && (
        <div style={{ marginTop: "10px" }}>
          <div>Uploading Jobs {progress}%</div>
          <div style={{ background: "#444", height: "10px" }}>
            <div
              style={{
                width: `${progress}%`,
                background: "#3b82f6",
                height: "10px",
              }}
            />
          </div>
        </div>
      )}

      {report && (
        <div style={{ marginTop: "10px" }}>
          <h4>Upload Report</h4>
          <p>Total: {report.total}</p>
          <p style={{ color: "green" }}>Valid: {report.success}</p>
          <p style={{ color: "red" }}>Invalid: {report.failed}</p>
        </div>
      )}
    </div>
  );
}

export default CsvUpload;