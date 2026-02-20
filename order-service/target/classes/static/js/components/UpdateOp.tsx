import React from 'react';

const UpdateComponent: React.FC = () => {
    return (
        <div className="p-4 border border-warning rounded bg-light shadow-sm">
            <h4 className="text-warning mb-3">Update Existing Item</h4>
            <div className="input-group mb-3">
                <input type="text" className="form-control" placeholder="Search by ID to update..." />
                <button className="btn btn-warning">Find</button>
            </div>
        </div>
    );
};

export default UpdateComponent;
