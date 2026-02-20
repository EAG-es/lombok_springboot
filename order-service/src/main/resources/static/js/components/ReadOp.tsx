import React from 'react';

const ReadComponent: React.FC = () => {
    return (
        <div className="p-4 border border-success rounded bg-light shadow-sm">
            <h4 className="text-success mb-3">View Items</h4>
            <table className="table table-hover">
                <thead className="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>Sample Data</td>
                        <td><span className="badge bg-info">Info</span></td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
};

export default ReadComponent;
