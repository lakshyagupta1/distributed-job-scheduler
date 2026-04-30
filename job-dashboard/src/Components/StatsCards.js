import React from "react";

function StatsCards({ jobs }) {

  const total = jobs.length;
  const success = jobs.filter(j => j.status === "SUCCESS").length;
  const failed = jobs.filter(j => j.status === "FAILED").length;
  const running = jobs.filter(j => j.status === "RUNNING").length;

  return (
    <div style={{ display: "flex", gap: "15px", marginBottom: "20px" }}>

      <Card title="Total Jobs" value={total} />
      <Card title="Success" value={success} color="#22c55e" />
      <Card title="Failed" value={failed} color="#ef4444" />
      <Card title="Running" value={running} color="#f59e0b" />

    </div>
  );
}

function Card({ title, value, color }) {
  return (
    <div className="card" style={{ flex: 1, textAlign: "center" }}>
      <div style={{ fontSize: "14px", color: "#94a3b8" }}>{title}</div>
      <div style={{ fontSize: "22px", color: color || "#e2e8f0" }}>
        {value}
      </div>
    </div>
  );
}

export default StatsCards;