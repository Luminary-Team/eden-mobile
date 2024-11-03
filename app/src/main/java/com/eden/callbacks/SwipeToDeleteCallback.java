package com.eden.callbacks;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;

abstract public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    Context mContext;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;


    protected SwipeToDeleteCallback(Context context) {
        mContext = context;
        mBackground = new ColorDrawable();
        backgroundColor = ContextCompat.getColor(context, R.color.edenCloudBlue);
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.delete_icon);
        deleteDrawable.setTint(ContextCompat.getColor(mContext, R.color.white));
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();
        float cornerRadius = 60.0f; // Ajuste conforme necessário

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setAntiAlias(true);

        // Criar Path para arredondar apenas as bordas direitas
        Path path = new Path();
        path.moveTo(itemView.getLeft(), itemView.getTop());
        path.lineTo(itemView.getRight() - cornerRadius, itemView.getTop());
        path.quadTo(itemView.getRight(), itemView.getTop(), itemView.getRight(), itemView.getTop() + cornerRadius);
        path.lineTo(itemView.getRight(), itemView.getBottom() - cornerRadius);
        path.quadTo(itemView.getRight(), itemView.getBottom(), itemView.getRight() - cornerRadius, itemView.getBottom());
        path.lineTo(itemView.getLeft(), itemView.getBottom());
        path.close();

        // Desenhar o fundo com cantos arredondados
        c.drawPath(path, paint);

        // Desenhe o ícone de exclusão
        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


//    @Override
//    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        View itemView = viewHolder.itemView;
//        int itemHeight = itemView.getHeight();
//
//        float cornerRadius = 30.0f;
//        boolean isCancelled = dX == 0 && !isCurrentlyActive;
//
//        if (isCancelled) {
//            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            return;
//        }
//
//        Paint paint = new Paint();
//        paint.setColor(backgroundColor);
//        paint.setAntiAlias(true);
//
//        RectF backgroundRect = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//        c.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, paint);
//
//        // Desenhe o ícone de exclusão
//        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
//        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
//        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
//        int deleteIconRight = itemView.getRight() - deleteIconMargin;
//        int deleteIconBottom = deleteIconTop + intrinsicHeight;
//
//        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
//        deleteDrawable.draw(c);
//
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}